import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import API from '../api/axios';

const Books = () => {
    const [books, setBooks] = useState([]);
    const [loading, setLoading] = useState(true);
    const [search, setSearch] = useState('');
    const [genre, setGenre] = useState('');
    const [page, setPage] = useState(0);
    const [totalPages, setTotalPages] = useState(0);

    const genres = ['Fiction', 'Fantasy', 'Self Help',
        'Mystery', 'Romance', 'Science', 'History'];

    const fetchBooks = async () => {
        setLoading(true);
        try {
            let res;
            if (search) {
                res = await API.get(
                    `/api/books/search?keyword=${search}&page=${page}&size=9`
                );
            } else if (genre) {
                res = await API.get(
                    `/api/books/filter?genre=${genre}&page=${page}&size=9`
                );
            } else {
                res = await API.get(`/api/books?page=${page}&size=9`);
            }
            setBooks(res.data.content);
            setTotalPages(res.data.totalPages);
        } catch (err) {
            console.error(err);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchBooks();
    }, [page, genre]);

    const handleSearch = (e) => {
        e.preventDefault();
        setPage(0);
        fetchBooks();
    };

    const conditionColor = (condition) => {
        const colors = {
            'NEW': 'bg-green-100 text-green-700',
            'LIKE_NEW': 'bg-blue-100 text-blue-700',
            'GOOD': 'bg-yellow-100 text-yellow-700',
            'FAIR': 'bg-orange-100 text-orange-700',
        };
        return colors[condition] || 'bg-gray-100 text-gray-700';
    };

    return (
        <div className="max-w-7xl mx-auto px-4 py-8">
            <h1 className="text-3xl font-bold text-gray-800 mb-8">
                Browse Books
            </h1>

            {/* Search and Filter */}
            <div className="bg-white p-4 rounded-2xl shadow-sm mb-8 flex gap-4 flex-wrap">
                <form onSubmit={handleSearch} className="flex gap-2 flex-1">
                    <input
                        type="text"
                        value={search}
                        onChange={(e) => setSearch(e.target.value)}
                        placeholder="Search by title or author..."
                        className="flex-1 border border-gray-300 rounded-lg px-4 py-2 focus:outline-none focus:ring-2 focus:ring-amber-400"
                    />
                    <button
                        type="submit"
                        className="bg-amber-500 text-white px-6 py-2 rounded-lg hover:bg-amber-600">
                        Search
                    </button>
                </form>

                <select
                    value={genre}
                    onChange={(e) => { setGenre(e.target.value); setPage(0); }}
                    className="border border-gray-300 rounded-lg px-4 py-2 focus:outline-none focus:ring-2 focus:ring-amber-400">
                    <option value="">All Genres</option>
                    {genres.map(g => (
                        <option key={g} value={g}>{g}</option>
                    ))}
                </select>
            </div>

            {/* Books Grid */}
            {loading ? (
                <div className="text-center py-20 text-gray-500">
                    Loading books...
                </div>
            ) : books.length === 0 ? (
                <div className="text-center py-20 text-gray-500">
                    No books found.
                </div>
            ) : (
                <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
                    {books.map(book => (
                        <Link key={book.id} to={`/books/${book.id}`}>
                            <div className="bg-white rounded-2xl shadow-sm hover:shadow-md transition p-6 h-full">
                                <div className="flex justify-between items-start mb-3">
                  <span className={`text-xs px-2 py-1 rounded-full font-medium ${conditionColor(book.condition)}`}>
                    {book.condition}
                  </span>
                                    {book.availableForExchange && (
                                        <span className="text-xs bg-amber-100 text-amber-700 px-2 py-1 rounded-full font-medium">
                      For Exchange
                    </span>
                                    )}
                                </div>
                                <h3 className="text-lg font-semibold text-gray-800 mb-1">
                                    {book.title}
                                </h3>
                                <p className="text-gray-500 text-sm mb-2">
                                    by {book.author}
                                </p>
                                <p className="text-xs text-gray-400 mb-3">
                                    {book.genre}
                                </p>
                                <div className="flex justify-between items-center mt-auto">
                  <span className="text-amber-600 font-bold">
                    ₹{book.price}
                  </span>
                                    <span className="text-xs text-gray-400">
                    by {book.ownerName}
                  </span>
                                </div>
                            </div>
                        </Link>
                    ))}
                </div>
            )}

            {/* Pagination */}
            {totalPages > 1 && (
                <div className="flex justify-center gap-2 mt-8">
                    <button
                        onClick={() => setPage(p => Math.max(0, p - 1))}
                        disabled={page === 0}
                        className="px-4 py-2 bg-white border rounded-lg disabled:opacity-50 hover:bg-amber-50">
                        Previous
                    </button>
                    <span className="px-4 py-2 text-gray-600">
            Page {page + 1} of {totalPages}
          </span>
                    <button
                        onClick={() => setPage(p => Math.min(totalPages - 1, p + 1))}
                        disabled={page === totalPages - 1}
                        className="px-4 py-2 bg-white border rounded-lg disabled:opacity-50 hover:bg-amber-50">
                        Next
                    </button>
                </div>
            )}
        </div>
    );
};

export default Books;