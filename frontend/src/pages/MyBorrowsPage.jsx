import { useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";
import { fetchMyBorrows, returnBook } from "../redux/borrowSlice";

function MyBorrowsPage() {
  const dispatch = useDispatch();
  const { memberId } = useSelector((state) => state.auth);
  const {
    items: borrows,
    loading,
    error,
  } = useSelector((state) => state.borrow);

  useEffect(() => {
    if (memberId) {
      dispatch(fetchMyBorrows(memberId));
    }
    // Only fetch once we actually have a memberId - guards against
    // firing the request before fetchCurrentMember() has resolved
  }, [dispatch, memberId]);

  const handleReturn = (borrowRecordId) => {
    dispatch(returnBook(borrowRecordId));
  };

  if (loading) {
    return <div className="p-8 text-espresso">Loading your borrows...</div>;
  }

  if (error) {
    return <div className="p-8 text-red-600">Error: {error}</div>;
  }

  const currentlyBorrowed = borrows.filter((b) => b.returnDate === null);
  const returned = borrows.filter((b) => b.returnDate !== null);
  // Split into two groups client-side - the backend already gives us
  // everything, we just organize it for display here

  return (
    <div className="min-h-screen bg-cream px-6 py-8">
      <h1 className="font-serif font-bold text-4xl text-espresso mb-6">
        My Borrows
      </h1>

      <section className="mb-10">
        <h2 className="font-serif font-semibold text-xl text-espresso mb-3">
          Currently Borrowed
        </h2>

        {currentlyBorrowed.length === 0 ? (
          <p className="text-espresso/60 text-sm">
            You have no books currently borrowed.
          </p>
        ) : (
          <div className="space-y-3">
            {currentlyBorrowed.map((record) => (
              <div
                key={record.id}
                className={`bg-white border rounded-lg p-4 flex items-center justify-between
                  ${record.overdue ? "border-red-300" : "border-espresso/10"}`}
              >
                <div>
                  <h3 className="font-serif font-bold text-espresso">
                    {record.book.title}
                  </h3>
                  <p className="text-sm text-espresso/60">
                    Due: {record.dueDate}
                    {record.overdue && (
                      <span className="ml-2 text-red-600 font-medium">
                        Overdue
                      </span>
                    )}
                  </p>
                </div>
                <button
                  onClick={() => handleReturn(record.id)}
                  className="bg-espresso text-cream text-sm font-medium px-4 py-2 rounded-md
                             hover:bg-espresso/90 transition-colors"
                >
                  Return
                </button>
              </div>
            ))}
          </div>
        )}
      </section>

      <section>
        <h2 className="font-serif font-semibold text-xl text-espresso mb-3">
          Borrow History
        </h2>

        {returned.length === 0 ? (
          <p className="text-espresso/60 text-sm">No past borrows yet.</p>
        ) : (
          <div className="space-y-3">
            {returned.map((record) => (
              <div
                key={record.id}
                className="bg-white/50 border border-espresso/10 rounded-lg p-4"
              >
                <h3 className="font-serif font-bold text-espresso">
                  {record.book.title}
                </h3>
                <p className="text-sm text-espresso/60">
                  Returned: {record.returnDate}
                  {record.fineAmount > 0 && (
                    <span className="ml-2 text-red-600">
                      Fine: Rs. {record.fineAmount} ({record.fineStatus})
                    </span>
                  )}
                </p>
              </div>
            ))}
          </div>
        )}
      </section>
    </div>
  );
}

export default MyBorrowsPage;
