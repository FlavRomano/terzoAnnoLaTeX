import requests
from bs4 import BeautifulSoup

url: str = "https://www.repubblica.it/"
page = requests.get(url)
soup = BeautifulSoup(page.text, 'html.parser')

print(soup.findAll("p"))
