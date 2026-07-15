import { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import {
  fetchCategories,
  createCategory,
  updateCategory,
  deleteCategory,
} from "../../redux/categoriesSlice";

function ManageCategoriesPage() {
  const dispatch = useDispatch();
  const {
    items: categories,
    loading,
    error,
  } = useSelector((state) => state.categories);

  const [name, setName] = useState("");
  const [editingId, setEditingId] = useState(null);

  useEffect(() => {
    dispatch(fetchCategories());
  }, [dispatch]);

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (editingId) {
      await dispatch(updateCategory({ id: editingId, categoryData: { name } }));
    } else {
      await dispatch(createCategory({ name }));
    }

    setName("");
    setEditingId(null);
  };

  const handleEdit = (category) => {
    setName(category.name);
    setEditingId(category.id);
  };

  const handleCancel = () => {
    setName("");
    setEditingId(null);
  };

  const handleDelete = (id) => {
    if (window.confirm("Delete this category? This cannot be undone.")) {
      dispatch(deleteCategory(id));
    }
  };

  return (
    <div className="min-h-screen bg-cream px-6 py-8">
      <h1 className="font-serif font-bold text-4xl text-espresso mb-6">
        Manage Categories
      </h1>

      <form
        onSubmit={handleSubmit}
        className="bg-white border border-espresso/10 rounded-lg p-4 mb-6 max-w-md"
      >
        <h2 className="font-serif font-semibold text-lg text-espresso mb-3">
          {editingId ? "Edit Category" : "Add New Category"}
        </h2>

        <div className="flex gap-2">
          <input
            type="text"
            value={name}
            onChange={(e) => setName(e.target.value)}
            required
            placeholder="Category name"
            className="flex-1 px-3 py-2 border border-espresso/20 rounded-md
                       focus:outline-none focus:ring-2 focus:ring-espresso/40 bg-white text-espresso"
          />
          <button
            type="submit"
            className="bg-espresso text-cream text-sm font-medium px-4 py-2 rounded-md
                       hover:bg-espresso/90 transition-colors"
          >
            {editingId ? "Save" : "Add"}
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
      </form>

      {loading && <p className="text-espresso/60">Loading categories...</p>}
      {error && <p className="text-red-600">Error: {error}</p>}

      <div className="flex flex-wrap gap-2">
        {categories.map((category) => (
          <div
            key={category.id}
            className="bg-white border border-espresso/10 rounded-full pl-4 pr-2 py-1.5
                       flex items-center gap-2"
          >
            <span className="text-sm text-espresso">{category.name}</span>
            <button
              onClick={() => handleEdit(category)}
              className="text-xs text-espresso/60 hover:text-espresso px-1"
            >
              Edit
            </button>
            <button
              onClick={() => handleDelete(category.id)}
              className="text-xs text-red-500 hover:text-red-700 px-1"
            >
              Delete
            </button>
          </div>
        ))}
      </div>
    </div>
  );
}

export default ManageCategoriesPage;
