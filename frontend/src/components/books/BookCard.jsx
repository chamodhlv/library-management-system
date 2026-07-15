function BookCard({ book, canBorrow, onBorrow }) {
  const isAvailable = book.availableCopies > 0;

  return (
    <div className="bg-white border border-espresso/10 rounded-lg p-4 shadow-sm hover:shadow-md transition-shadow">
      <h3 className="font-serif font-bold text-lg text-espresso mb-1">
        {book.title}
      </h3>
      <p className="text-sm text-espresso/60 mb-2">
        {book.authors.map((a) => a.name).join(", ")}
      </p>
      <div className="flex flex-wrap gap-1 mb-3">
        {book.categories.map((cat) => (
          <span
            key={cat.id}
            className="text-xs bg-espresso/10 text-espresso px-2 py-0.5 rounded-full"
          >
            {cat.name}
          </span>
        ))}
      </div>
      <p className="text-sm text-espresso/80 mb-3">
        {book.availableCopies} of {book.totalCopies} available
      </p>

      {canBorrow && (
        <button
          onClick={onBorrow}
          disabled={!isAvailable}
          className="w-full bg-espresso text-cream text-sm font-medium py-2 rounded-md
                     hover:bg-espresso/90 transition-colors disabled:opacity-40
                     disabled:cursor-not-allowed"
        >
          {isAvailable ? "Borrow" : "Unavailable"}
        </button>
      )}
    </div>
  );
}

export default BookCard;
