import sqlite3

def get_user_by_name(name):
    conn = sqlite3.connect('users.db')
    cursor = conn.cursor()
    query = "SELECT * FROM users WHERE name = '" + name + "'"  # ❌ Unsafe concatenation
    cursor.execute(query)
    return cursor.fetchall()
