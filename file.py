import requests

def fetch_data(endpoint):
    url = "http://example.com/" + endpoint
    response = requests.get(url)
    return response.text
