import { useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";
import { fetchBooks } from "../redux/booksSlice";
import { borrowBook } from "../redux/borrowSlice";
import BookCard from "../components/books/BookCard";

function CatalogPage() {
  const dispatch = useDispatch();
  const { items: books, loading, error } = useSelector((state) => state.books);
  const { isAuthenticated, role, memberId } = useSelector(
    (state) => state.auth,
  );

  useEffect(() => {
    dispatch(fetchBooks());
  }, [dispatch]);

  const handleBorrow = async (bookId) => {
    await dispatch(borrowBook({ memberId, bookId }));
    dispatch(fetchBooks());
    // Re-fetch so availableCopies updates on screen immediately
  };

  if (loading) {
    return (
      <div className="min-h-screen bg-cream flex items-center justify-center text-espresso">
        Loading books...
      </div>
    );
  }

  if (error) {
    return (
      <div className="min-h-screen bg-cream flex items-center justify-center text-red-600">
        Error: {error}
      </div>
    );
  }

  const canBorrow = isAuthenticated && role === "MEMBER";

  return (
    <div className="min-h-screen bg-cream px-6 py-8">
      <h1 className="font-serif font-bold text-4xl text-espresso mb-6">
        Library Catalog
      </h1>
      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-4">
        {books.map((book) => (
          <BookCard
            key={book.id}
            book={book}
            canBorrow={canBorrow}
            onBorrow={() => handleBorrow(book.id)}
          />
        ))}
      </div>
    </div>
  );
}

export default CatalogPage;
