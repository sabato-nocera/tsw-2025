def _configure_database():
    username = "admin"
    password = "admin"
    return db.connect(username, password)
