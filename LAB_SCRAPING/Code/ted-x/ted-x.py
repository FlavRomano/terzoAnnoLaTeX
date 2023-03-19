from bs4 import BeautifulSoup
import seaborn as sns
import pandas as pd
import numpy as np
import datetime
import matplotlib.pyplot as plt


df = pd.read_csv("./datasets/ted_main.csv")
df["film_date"] = df["film_date"].apply(
    lambda x: datetime.datetime.fromtimestamp(int(x)).strftime("%d-%m-%y"))
df["published_date"] = df["published_date"].apply(
    lambda x: datetime.datetime.fromtimestamp(int(x)).strftime("%d-%m-%y"))

pop_talks = df[["title", "main_speaker", "views", "film_date"]
               ].sort_values("views", ascending=False)
# print(pop_talks)

pop_talks['abbr'] = pop_talks['main_speaker'].apply(lambda x: x[:3])
sns.set_style("whitegrid")
sns.barplot(x='abbr', y='views', data=pop_talks)

brev_talks = df.groupby(by="main_speaker").agg(
    talks=pd.NamedAgg(column="main_speaker", aggfunc="count"))
# reset_index(inplace=True) reimposta l'indexing di partenza
brev_talks.reset_index(inplace=True)
brev_talks = brev_talks.sort_values("talks", ascending=False)
print(brev_talks.head(10))
