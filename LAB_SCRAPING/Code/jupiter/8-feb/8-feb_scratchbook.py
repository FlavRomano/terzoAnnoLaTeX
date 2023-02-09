from datetime import datetime

import warnings
import matplotlib.dates as mdates
import matplotlib.pyplot as plt
import seaborn as sns
import pandas as pd
import numpy as np

warnings.filterwarnings('ignore')

df = pd.DataFrame([[2, 3], [5, 6], [8, 9]],
                  index=['cobra', 'viper', 'sidewinder'],
                  columns=['max_speed', 'shield'])

# print(df.loc[df["shield"] >= 6], "\n")
# print(df.loc[df["shield"] >= 6, ["max_speed"]])

np.random.seed(0)
company = ["A", "B", "C"]
data_company = pd.DataFrame({
    "company": [company[x] for x in np.random.randint(0, len(company), 10)],
    "salary": np.random.randint(5, 50, 10),
    "age": np.random.randint(15, 50, 10)}
)
group = data_company.groupby("company")

# print(data_company, "\n")
# print(list(group))
