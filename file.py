import sqlite3

def get_user_by_name(value):
    with connection.cursor() as cursor:
        cursor.execute("{0}".format(value))
        return cursor.fetchall()
