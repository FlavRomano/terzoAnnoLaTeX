import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
from pandas.plotting import *

np.random.seed(0)

Regions = pd.Series(['Toscana', 'Sicilia', 'Puglia', 'Sardegna'],
                    ['Firenze', 'Palermo', 'Bari', 'Cagliari'])

# print(Regions)
# print(Regions[0])
# print('Palermo' in Regions)
# print('Sicilia' in Regions)

df = pd.DataFrame({
    "Flower": ["Violet", "Daisy"],
    "Month": ["Feb", "Apr"]
})

print(df, "\n")
#    Flower Month
# 0  Violet   Feb
# 1   Daisy   Apr
print(df["Flower"], "\n")
# 0    Violet
# 1     Daisy
print(df["Month"], "\n")
# 0    Feb
# 1    Apr
print(df.iloc[0], "\n")
# Flower    Violet
# Month        Feb

df_iris = pd.read_csv("jupiter/7-feb/iris.csv")
len_row = df_iris.shape[0]
len_col = df_iris.shape[1]
print("Dataset iris:\n", df_iris, "\n")
# print(df_iris.info())
# print(df_iris.describe(include=object))

np.random.permutation(len_row)  # numero di righe del dataset

small_df_iris = df_iris.iloc[np.random.permutation(len_row)].head()
print("Small iris:\n", small_df_iris, "\n")

media = small_df_iris.mean(numeric_only=True)
print("Media:\n", media)

df_iris[df_iris["variety"] == "Setosa"].plot()
df_iris.hist()
plt.show()
