from bs4 import BeautifulSoup
from urllib.request import urlopen

html = urlopen("https://pages.di.unipi.it/ricci/")
bs = BeautifulSoup(html.read(), "html.parser")
