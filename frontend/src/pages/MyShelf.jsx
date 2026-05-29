import React, { useState, useEffect } from 'react';
import API from '../api/axios';
import { useAuth } from '../context/AuthContext';
import { useNavigate } from 'react-router-dom';

const MyShelf = () => {
    const [books, setBooks] = useState([]);
    const [loading, setLoading] = useState(true);
    const [showForm, setShowForm] = useState(false);
    const [form, setForm] = useState({
        title: '', author: '', genre: '',
        condition: 'GOOD', price: '',
        description: '', availableForExchange: true
    });
    const { user } = useAuth();
    const navigate = useNavigate();

    useEffect(() => {
        if (!user) { navigate('/login'); return; }
        fetchMyBooks();
    }, [user]);

    const fetchMyBooks = async () => {
        try {
            const res = await API.get('/api/books/my-books');
            setBooks(res.data);
        } catch (err) {
            console.error(err);
        } finally {
            setLoading(false);
        }
    };

    const handleAddBook = async (e) => {
        e.preventDefault();
        try {
            await API.post('/api/books', {
                ...form,
                price: parseFloat(form.price)
            });
            setShowForm(false);
            setForm({
                title: '', author: '', genre: '',
                condition: 'GOOD', price: '',
                description: '', availableForExchange: true
            });
            fetchMyBooks();
        } catch (err) {
            console.error(err);
        }
    };

    return (
        <div className="max-w-7xl mx-auto px-4 py-8">
            <div className="flex justify-between items-center mb-8">
                <h1 className="text-3xl font-bold text-gray-800">My Shelf</h1>
                <button
                    onClick={() => setShowForm(!showForm)}
                    className="bg-amber-500 text-white px-6 py-2 rounded-lg hover:bg-amber-600">
                    + Add Book
                </button>
            </div>

            {/* Add Book Form */}
            {showForm && (
                <div className="bg-white p-6 rounded-2xl shadow-sm mb-8">
                    <h2 className="text-xl font-semibold mb-4">Add New Book</h2>
                    <form onSubmit={handleAddBook}
                          className="grid grid-cols-1 md:grid-cols-2 gap-4">
                        <input
                            placeholder="Title *"
                            value={form.title}
                            onChange={(e) => setForm({...form, title: e.target.value})}
                            className="border rounded-lg px-4 py-2 focus:outline-none focus:ring-2 focus:ring-amber-400"
                            required
                        />
                        <input
                            placeholder="Author *"
                            value={form.author}
                            onChange={(e) => setForm({...form, author: e.target.value})}
                            className="border rounded-lg px-4 py-2 focus:outline-none focus:ring-2 focus:ring-amber-400"
                            required
                        />
                        <input
                            placeholder="Genre"
                            value={form.genre}
                            onChange={(e) => setForm({...form, genre: e.target.value})}
                            className="border rounded-lg px-4 py-2 focus:outline-none focus:ring-2 focus:ring-amber-400"
                        />
                        <input
                            placeholder="Price (₹)"
                            type="number"
                            value={form.price}
                            onChange={(e) => setForm({...form, price: e.target.value})}
                            className="border rounded-lg px-4 py-2 focus:outline-none focus:ring-2 focus:ring-amber-400"
                        />
                        <select
                            value={form.condition}
                            onChange={(e) => setForm({...form, condition: e.target.value})}
                            className="border rounded-lg px-4 py-2 focus:outline-none focus:ring-2 focus:ring-amber-400">
                            <option value="NEW">New</option>
                            <option value="LIKE_NEW">Like New</option>
                            <option value="GOOD">Good</option>
                            <option value="FAIR">Fair</option>
                        </select>
                        <label className="flex items-center gap-2">
                            <input
                                type="checkbox"
                                checked={form.availableForExchange}
                                onChange={(e) => setForm({
                                    ...form,
                                    availableForExchange: e.target.checked
                                })}
                                className="w-4 h-4 accent-amber-500"
                            />
                            <span className="text-gray-700">Available for exchange</span>
                        </label>
                        <textarea
                            placeholder="Description"
                            value={form.description}
                            onChange={(e) => setForm({...form, description: e.target.value})}
                            className="border rounded-lg px-4 py-2 focus:outline-none focus:ring-2 focus:ring-amber-400 md:col-span-2"
                            rows={3}
                        />
                        <div className="md:col-span-2 flex gap-3">
                            <button
                                type="submit"
                                className="bg-amber-500 text-white px-6 py-2 rounded-lg hover:bg-amber-600">
                                Add Book
                            </button>
                            <button
                                type="button"
                                onClick={() => setShowForm(false)}
                                className="bg-gray-100 text-gray-700 px-6 py-2 rounded-lg hover:bg-gray-200">
                                Cancel
                            </button>
                        </div>
                    </form>
                </div>
            )}

            {/* Books List */}
            {loading ? (
                <div className="text-center py-20 text-gray-500">Loading...</div>
            ) : books.length === 0 ? (
                <div className="text-center py-20">
                    <p className="text-gray-500 text-lg mb-4">
                        Your shelf is empty!
                    </p>
                    <button
                        onClick={() => setShowForm(true)}
                        className="bg-amber-500 text-white px-6 py-3 rounded-lg hover:bg-amber-600">
                        Add your first book
                    </button>
                </div>
            ) : (
                <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
                    {books.map(book => (
                        <div key={book.id}
                             className="bg-white rounded-2xl shadow-sm p-6">
                            <h3 className="text-lg font-semibold text-gray-800 mb-1">
                                {book.title}
                            </h3>
                            <p className="text-gray-500 text-sm mb-3">
                                by {book.author}
                            </p>
                            <div className="flex justify-between items-center">
                <span className="text-amber-600 font-bold">
                  ₹{book.price}
                </span>
                                <span className={`text-xs px-2 py-1 rounded-full
                  ${book.status === 'AVAILABLE'
                                    ? 'bg-green-100 text-green-700'
                                    : 'bg-gray-100 text-gray-500'}`}>
                  {book.status}
                </span>
                            </div>
                        </div>
                    ))}
                </div>
            )}
        </div>
    );
};

export default MyShelf;