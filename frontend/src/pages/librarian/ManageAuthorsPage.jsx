import { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import {
  fetchAuthors,
  createAuthor,
  updateAuthor,
  deleteAuthor,
} from "../../redux/authorsSlice";

function ManageAuthorsPage() {
  const dispatch = useDispatch();
  const {
    items: authors,
    loading,
    error,
  } = useSelector((state) => state.authors);

  const [formData, setFormData] = useState({ name: "", bio: "" });
  const [editingId, setEditingId] = useState(null);
  // null = creating a new author; a number = editing that author's id

  useEffect(() => {
    dispatch(fetchAuthors());
  }, [dispatch]);

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (editingId) {
      await dispatch(updateAuthor({ id: editingId, authorData: formData }));
    } else {
      await dispatch(createAuthor(formData));
    }

    setFormData({ name: "", bio: "" });
    setEditingId(null);
  };

  const handleEdit = (author) => {
    setFormData({ name: author.name, bio: author.bio || "" });
    setEditingId(author.id);
  };

  const handleCancel = () => {
    setFormData({ name: "", bio: "" });
    setEditingId(null);
  };

  const handleDelete = (id) => {
    if (window.confirm("Delete this author? This cannot be undone.")) {
      dispatch(deleteAuthor(id));
    }
  };

  return (
    <div className="min-h-screen bg-cream px-6 py-8">
      <h1 className="font-serif font-bold text-4xl text-espresso mb-6">
        Manage Authors
      </h1>

      <form
        onSubmit={handleSubmit}
        className="bg-white border border-espresso/10 rounded-lg p-4 mb-6 max-w-lg"
      >
        <h2 className="font-serif font-semibold text-lg text-espresso mb-3">
          {editingId ? "Edit Author" : "Add New Author"}
        </h2>

        <div className="space-y-3">
          <div>
            <label className="block text-sm font-medium text-espresso mb-1">
              Name
            </label>
            <input
              type="text"
              name="name"
              value={formData.name}
              onChange={handleChange}
              required
              className="w-full px-3 py-2 border border-espresso/20 rounded-md
                         focus:outline-none focus:ring-2 focus:ring-espresso/40 bg-white text-espresso"
            />
          </div>

          <div>
            <label className="block text-sm font-medium text-espresso mb-1">
              Bio
            </label>
            <textarea
              name="bio"
              value={formData.bio}
              onChange={handleChange}
              rows={3}
              className="w-full px-3 py-2 border border-espresso/20 rounded-md
                         focus:outline-none focus:ring-2 focus:ring-espresso/40 bg-white text-espresso"
            />
          </div>

          <div className="flex gap-2">
            <button
              type="submit"
              className="bg-espresso text-cream text-sm font-medium px-4 py-2 rounded-md
                         hover:bg-espresso/90 transition-colors"
            >
              {editingId ? "Save Changes" : "Add Author"}
            </button>
            {editingId && (
              <button
                type="button"
                onClick={handleCancel}
                className="text-espresso text-sm font-medium px-4 py-2 rounded-md
                           hover:bg-espresso/10 transition-colors"
              >
                Cancel
              </button>
            )}
          </div>
        </div>
      </form>

      {loading && <p className="text-espresso/60">Loading authors...</p>}
      {error && <p className="text-red-600">Error: {error}</p>}

      <div className="space-y-2">
        {authors.map((author) => (
          <div
            key={author.id}
            className="bg-white border border-espresso/10 rounded-lg p-4 flex items-center justify-between"
          >
            <div>
              <h3 className="font-serif font-bold text-espresso">
                {author.name}
              </h3>
              {author.bio && (
                <p className="text-sm text-espresso/60 mt-1">{author.bio}</p>
              )}
            </div>
            <div className="flex gap-2">
              <button
                onClick={() => handleEdit(author)}
                className="text-sm text-espresso font-medium px-3 py-1.5 rounded-md hover:bg-espresso/10"
              >
                Edit
              </button>
              <button
                onClick={() => handleDelete(author.id)}
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

export default ManageAuthorsPage;
