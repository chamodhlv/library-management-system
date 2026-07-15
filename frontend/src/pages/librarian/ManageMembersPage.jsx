import { useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";
import {
  fetchMembers,
  updateMemberRole,
  deleteMember,
} from "../../redux/membersSlice";

function ManageMembersPage() {
  const dispatch = useDispatch();
  const {
    items: members,
    loading,
    error,
  } = useSelector((state) => state.members);
  const { email: currentUserEmail } = useSelector((state) => state.auth);
  // We track the logged-in user's own email so we can prevent them from
  // demoting/deleting themselves by accident

  useEffect(() => {
    dispatch(fetchMembers());
  }, [dispatch]);

  const handleToggleRole = (member) => {
    const newRole = member.role === "LIBRARIAN" ? "MEMBER" : "LIBRARIAN";
    const action = newRole === "LIBRARIAN" ? "promote" : "demote";

    if (
      window.confirm(
        `${action === "promote" ? "Promote" : "Demote"} ${member.name} to ${newRole}?`,
      )
    ) {
      dispatch(updateMemberRole({ id: member.id, role: newRole }));
    }
  };

  const handleDelete = (member) => {
    if (window.confirm(`Delete ${member.name}? This cannot be undone.`)) {
      dispatch(deleteMember(member.id));
    }
  };

  if (loading) {
    return <div className="p-8 text-espresso">Loading members...</div>;
  }

  if (error) {
    return <div className="p-8 text-red-600">Error: {error}</div>;
  }

  return (
    <div className="min-h-screen bg-cream px-6 py-8">
      <h1 className="font-serif font-bold text-4xl text-espresso mb-6">
        Manage Members
      </h1>

      <div className="bg-white border border-espresso/10 rounded-lg overflow-hidden">
        <table className="w-full text-left">
          <thead className="bg-espresso/5 border-b border-espresso/10">
            <tr>
              <th className="px-4 py-3 text-sm font-medium text-espresso">
                Name
              </th>
              <th className="px-4 py-3 text-sm font-medium text-espresso">
                Email
              </th>
              <th className="px-4 py-3 text-sm font-medium text-espresso">
                Role
              </th>
              <th className="px-4 py-3 text-sm font-medium text-espresso">
                Member Since
              </th>
              <th className="px-4 py-3 text-sm font-medium text-espresso">
                Actions
              </th>
            </tr>
          </thead>
          <tbody>
            {members.map((member) => {
              const isSelf = member.email === currentUserEmail;

              return (
                <tr
                  key={member.id}
                  className="border-b border-espresso/5 last:border-0"
                >
                  <td className="px-4 py-3 text-sm text-espresso">
                    {member.name}
                  </td>
                  <td className="px-4 py-3 text-sm text-espresso/80">
                    {member.email}
                  </td>
                  <td className="px-4 py-3 text-sm">
                    <span
                      className={`text-xs px-2 py-0.5 rounded-full ${
                        member.role === "LIBRARIAN"
                          ? "bg-espresso text-cream"
                          : "bg-espresso/10 text-espresso"
                      }`}
                    >
                      {member.role}
                    </span>
                  </td>
                  <td className="px-4 py-3 text-sm text-espresso/80">
                    {member.membershipDate}
                  </td>
                  <td className="px-4 py-3 text-sm">
                    {isSelf ? (
                      <span className="text-espresso/40 text-xs">You</span>
                    ) : (
                      <div className="flex gap-2">
                        <button
                          onClick={() => handleToggleRole(member)}
                          className="text-espresso font-medium hover:underline"
                        >
                          {member.role === "LIBRARIAN" ? "Demote" : "Promote"}
                        </button>
                        <button
                          onClick={() => handleDelete(member)}
                          className="text-red-600 font-medium hover:underline"
                        >
                          Delete
                        </button>
                      </div>
                    )}
                  </td>
                </tr>
              );
            })}
          </tbody>
        </table>
      </div>
    </div>
  );
}

export default ManageMembersPage;
