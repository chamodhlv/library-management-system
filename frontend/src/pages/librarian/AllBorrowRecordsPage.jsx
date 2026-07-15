import { useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";
import { fetchAllBorrowRecords } from "../../redux/borrowSlice";

function AllBorrowRecordsPage() {
  const dispatch = useDispatch();
  const { allRecords, loading, error } = useSelector((state) => state.borrow);

  useEffect(() => {
    dispatch(fetchAllBorrowRecords());
  }, [dispatch]);

  if (loading) {
    return <div className="p-8 text-espresso">Loading borrow records...</div>;
  }

  if (error) {
    return <div className="p-8 text-red-600">Error: {error}</div>;
  }

  return (
    <div className="min-h-screen bg-cream px-6 py-8">
      <h1 className="font-serif font-bold text-4xl text-espresso mb-6">
        All Borrow Records
      </h1>
      <p className="text-sm text-espresso/60 mb-4">
        Showing all books currently checked out across the library.
      </p>

      {allRecords.length === 0 ? (
        <p className="text-espresso/60">No books are currently borrowed.</p>
      ) : (
        <div className="bg-white border border-espresso/10 rounded-lg overflow-hidden">
          <table className="w-full text-left">
            <thead className="bg-espresso/5 border-b border-espresso/10">
              <tr>
                <th className="px-4 py-3 text-sm font-medium text-espresso">
                  Book
                </th>
                <th className="px-4 py-3 text-sm font-medium text-espresso">
                  Borrowed By
                </th>
                <th className="px-4 py-3 text-sm font-medium text-espresso">
                  Borrow Date
                </th>
                <th className="px-4 py-3 text-sm font-medium text-espresso">
                  Due Date
                </th>
                <th className="px-4 py-3 text-sm font-medium text-espresso">
                  Status
                </th>
              </tr>
            </thead>
            <tbody>
              {allRecords.map((record) => (
                <tr
                  key={record.id}
                  className="border-b border-espresso/5 last:border-0"
                >
                  <td className="px-4 py-3 text-sm text-espresso">
                    {record.book.title}
                  </td>
                  <td className="px-4 py-3 text-sm text-espresso/80">
                    {record.member.name}
                    <span className="block text-xs text-espresso/50">
                      {record.member.email}
                    </span>
                  </td>
                  <td className="px-4 py-3 text-sm text-espresso/80">
                    {record.borrowDate}
                  </td>
                  <td className="px-4 py-3 text-sm text-espresso/80">
                    {record.dueDate}
                  </td>
                  <td className="px-4 py-3 text-sm">
                    {record.overdue ? (
                      <span className="text-xs bg-red-100 text-red-700 px-2 py-0.5 rounded-full">
                        Overdue
                      </span>
                    ) : (
                      <span className="text-xs bg-green-100 text-green-700 px-2 py-0.5 rounded-full">
                        On Time
                      </span>
                    )}
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </div>
  );
}

export default AllBorrowRecordsPage;
