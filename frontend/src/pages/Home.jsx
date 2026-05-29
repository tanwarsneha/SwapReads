import React from 'react';
import { Link } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

const Home = () => {
    const { user } = useAuth();

    return (
        <div>
            {/* Hero Section */}
            <div className="bg-gradient-to-br from-amber-50 to-orange-100 py-20">
                <div className="max-w-7xl mx-auto px-4 text-center">
                    <h1 className="text-5xl font-bold text-gray-800 mb-6">
                        📚 Swap Books, <span className="text-amber-600">Share Stories</span>
                    </h1>
                    <p className="text-xl text-gray-600 mb-8 max-w-2xl mx-auto">
                        SwapReads is a community where book lovers exchange their
                        favourite reads. List your books, discover new ones, and
                        swap with readers near you!
                    </p>
                    <div className="flex gap-4 justify-center">
                        <Link to="/books"
                              className="bg-amber-500 text-white px-8 py-3 rounded-xl font-semibold text-lg hover:bg-amber-600">
                            Browse Books
                        </Link>
                        {!user && (
                            <Link to="/register"
                                  className="bg-white text-amber-600 border-2 border-amber-500 px-8 py-3 rounded-xl font-semibold text-lg hover:bg-amber-50">
                                Join Free
                            </Link>
                        )}
                    </div>
                </div>
            </div>

            {/* Features Section */}
            <div className="max-w-7xl mx-auto px-4 py-16">
                <h2 className="text-3xl font-bold text-center text-gray-800 mb-12">
                    How SwapReads Works
                </h2>
                <div className="grid grid-cols-1 md:grid-cols-3 gap-8">
                    <div className="bg-white p-6 rounded-2xl shadow-sm text-center">
                        <div className="text-4xl mb-4">📖</div>
                        <h3 className="text-xl font-semibold mb-2">List Your Books</h3>
                        <p className="text-gray-500">Add books from your shelf that
                            you're ready to share with others.</p>
                    </div>
                    <div className="bg-white p-6 rounded-2xl shadow-sm text-center">
                        <div className="text-4xl mb-4">🔍</div>
                        <h3 className="text-xl font-semibold mb-2">Discover Books</h3>
                        <p className="text-gray-500">Browse thousands of books listed
                            by readers in your community.</p>
                    </div>
                    <div className="bg-white p-6 rounded-2xl shadow-sm text-center">
                        <div className="text-4xl mb-4">🤝</div>
                        <h3 className="text-xl font-semibold mb-2">Swap & Enjoy</h3>
                        <p className="text-gray-500">Send a swap request, agree on
                            terms, and exchange books!</p>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default Home;