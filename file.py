from django.db import connection

def get_user_by_name(value):
    with connection.cursor() as cursor:
        cursor.execute("{0}".format(value))
        return cursor.fetchall()
