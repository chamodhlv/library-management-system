import { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import {
  fetchBooks,
  createBook,
  updateBook,
  deleteBook,
} from "../../redux/booksSlice";
import { fetchAuthors } from "../../redux/authorsSlice";
import { fetchCategories } from "../../redux/categoriesSlice";

const emptyForm = {
  title: "",
  isbn: "",
  totalCopies: 1,
  authorIds: [],
  categoryIds: [],
};

function ManageBooksPage() {
  const dispatch = useDispatch();
  const { items: books, loading, error } = useSelector((state) => state.books);
  const { items: authors } = useSelector((state) => state.authors);
  const { items: categories } = useSelector((state) => state.categories);

  const [formData, setFormData] = useState(emptyForm);
  const [editingId, setEditingId] = useState(null);

  useEffect(() => {
    dispatch(fetchBooks());
    dispatch(fetchAuthors());
    dispatch(fetchCategories());
    // Need all three - the form's checkboxes are built from authors/categories lists
  }, [dispatch]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({
      ...formData,
      [name]: name === "totalCopies" ? Number(value) : value,
    });
  };

  const toggleCheckbox = (field, id) => {
    setFormData((prev) => {
      const current = prev[field];
      const updated = current.includes(id)
        ? current.filter((existingId) => existingId !== id)
        : [...current, id];
      return { ...prev, [field]: updated };
    });
    // Generic toggle - works for both authorIds and categoryIds by passing
    // the field name as a parameter, same idea as the generic handleChange pattern
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (editingId) {
      await dispatch(updateBook({ id: editingId, bookData: formData }));
    } else {
      await dispatch(createBook(formData));
    }

    setFormData(emptyForm);
    setEditingId(null);
  };

  const handleEdit = (book) => {
    setFormData({
      title: book.title,
      isbn: book.isbn,
      totalCopies: book.totalCopies,
      authorIds: book.authors.map((a) => a.id),
      categoryIds: book.categories.map((c) => c.id),
      // Backend returns full author/category objects in BookResponseDto -
      // we just need their ids to pre-fill the form's checkboxes
    });
    setEditingId(book.id);
  };

  const handleCancel = () => {
    setFormData(emptyForm);
    setEditingId(null);
  };

  const handleDelete = (id) => {
    if (window.confirm("Delete this book? This cannot be undone.")) {
      dispatch(deleteBook(id));
    }
  };

  return (
    <div className="min-h-screen bg-cream px-6 py-8">
      <h1 className="font-serif font-bold text-4xl text-espresso mb-6">
        Manage Books
      </h1>

      <form
        onSubmit={handleSubmit}
        className="bg-white border border-espresso/10 rounded-lg p-4 mb-6 max-w-2xl"
      >
        <h2 className="font-serif font-semibold text-lg text-espresso mb-3">
          {editingId ? "Edit Book" : "Add New Book"}
        </h2>

        <div className="space-y-3">
          <div className="grid grid-cols-2 gap-3">
            <div>
              <label className="block text-sm font-medium text-espresso mb-1">
                Title
              </label>
              <input
                type="text"
                name="title"
                value={formData.title}
                onChange={handleChange}
                required
                className="w-full px-3 py-2 border border-espresso/20 rounded-md
                           focus:outline-none focus:ring-2 focus:ring-espresso/40 bg-white text-espresso"
              />
            </div>
            <div>
              <label className="block text-sm font-medium text-espresso mb-1">
                ISBN
              </label>
              <input
                type="text"
                name="isbn"
                value={formData.isbn}
                onChange={handleChange}
                required
                className="w-full px-3 py-2 border border-espresso/20 rounded-md
                           focus:outline-none focus:ring-2 focus:ring-espresso/40 bg-white text-espresso"
              />
            </div>
          </div>

          <div>
            <label className="block text-sm font-medium text-espresso mb-1">
              Total Copies
            </label>
            <input
              type="number"
              name="totalCopies"
              value={formData.totalCopies}
              onChange={handleChange}
              min={1}
              required
              className="w-32 px-3 py-2 border border-espresso/20 rounded-md
                         focus:outline-none focus:ring-2 focus:ring-espresso/40 bg-white text-espresso"
            />
          </div>

          <div>
            <label className="block text-sm font-medium text-espresso mb-1">
              Authors
            </label>
            <div className="flex flex-wrap gap-2">
              {authors.map((author) => (
                <label
                  key={author.id}
                  className={`text-sm px-3 py-1.5 rounded-full border cursor-pointer transition-colors
                    ${
                      formData.authorIds.includes(author.id)
                        ? "bg-espresso text-cream border-espresso"
                        : "bg-white text-espresso border-espresso/20"
                    }`}
                >
                  <input
                    type="checkbox"
                    checked={formData.authorIds.includes(author.id)}
                    onChange={() => toggleCheckbox("authorIds", author.id)}
                    className="hidden"
                  />
                  {author.name}
                </label>
              ))}
            </div>
          </div>

          <div>
            <label className="block text-sm font-medium text-espresso mb-1">
              Categories
            </label>
            <div className="flex flex-wrap gap-2">
              {categories.map((category) => (
                <label
                  key={category.id}
                  className={`text-sm px-3 py-1.5 rounded-full border cursor-pointer transition-colors
                    ${
                      formData.categoryIds.includes(category.id)
                        ? "bg-espresso text-cream border-espresso"
                        : "bg-white text-espresso border-espresso/20"
                    }`}
                >
                  <input
                    type="checkbox"
                    checked={formData.categoryIds.includes(category.id)}
                    onChange={() => toggleCheckbox("categoryIds", category.id)}
                    className="hidden"
                  />
                  {category.name}
                </label>
              ))}
            </div>
          </div>

          <div className="flex gap-2 pt-2">
            <button
              type="submit"
              className="bg-espresso text-cream text-sm font-medium px-4 py-2 rounded-md
                         hover:bg-espresso/90 transition-colors"
            >
              {editingId ? "Save Changes" : "Add Book"}
            </button>
            {editingId && (
              <button
                type="button"
                onClick={handleCancel}
                className="text-espresso text-sm font-medium px-4 py-2 rounded-md hover:bg-espresso/10"
              >
                Cancel
              </button>
            )}
          </div>
        </div>
      </form>

      {loading && <p className="text-espresso/60">Loading books...</p>}
      {error && <p className="text-red-600">Error: {error}</p>}

      <div className="space-y-2">
        {books.map((book) => (
          <div
            key={book.id}
            className="bg-white border border-espresso/10 rounded-lg p-4 flex items-center justify-between"
          >
            <div>
              <h3 className="font-serif font-bold text-espresso">
                {book.title}
              </h3>
              <p className="text-sm text-espresso/60">
                {book.authors.map((a) => a.name).join(", ")} · {book.isbn}
              </p>
              <p className="text-sm text-espresso/80 mt-1">
                {book.availableCopies} of {book.totalCopies} available
              </p>
            </div>
            <div className="flex gap-2">
              <button
                onClick={() => handleEdit(book)}
                className="text-sm text-espresso font-medium px-3 py-1.5 rounded-md hover:bg-espresso/10"
              >
                Edit
              </button>
              <button
                onClick={() => handleDelete(book.id)}
                className="text-sm text-red-600 font-medium px-3 py-1.5 rounded-md hover:bg-red-50"
              >
                Delete
              </button>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}

export default ManageBooksPage;
