import React from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

const Navbar = () => {
    const { user, logout } = useAuth();
    const navigate = useNavigate();

    const handleLogout = () => {
        logout();
        navigate('/');
    };

    return (
        <nav className="bg-white shadow-md sticky top-0 z-50">
            <div className="max-w-7xl mx-auto px-4 py-3 flex justify-between items-center">
                <Link to="/" className="text-2xl font-bold text-amber-600">
                    📚 SwapReads
                </Link>

                <div className="flex items-center gap-6">
                    <Link to="/books"
                          className="text-gray-600 hover:text-amber-600 font-medium">
                        Browse Books
                    </Link>

                    {user ? (
                        <>
                            <Link to="/my-shelf"
                                  className="text-gray-600 hover:text-amber-600 font-medium">
                                My Shelf
                            </Link>
                            <Link to="/exchange"
                                  className="text-gray-600 hover:text-amber-600 font-medium">
                                Exchanges
                            </Link>
                            <span className="text-gray-500 text-sm">
                Hi, {user.name}!
              </span>
                            <button
                                onClick={handleLogout}
                                className="bg-amber-500 text-white px-4 py-2 rounded-lg hover:bg-amber-600">
                                Logout
                            </button>
                        </>
                    ) : (
                        <>
                            <Link to="/login"
                                  className="text-gray-600 hover:text-amber-600 font-medium">
                                Login
                            </Link>
                            <Link to="/register"
                                  className="bg-amber-500 text-white px-4 py-2 rounded-lg hover:bg-amber-600">
                                Sign Up
                            </Link>
                        </>
                    )}
                </div>
            </div>
        </nav>
    );
};

export default Navbar;