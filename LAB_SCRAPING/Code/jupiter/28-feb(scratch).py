"""
https://www.eia.gov/
Reperire i dati annuali relativi al prezo dell'energia elettrica a partire dal 31/01/2021
"""
import pandas as pd
import requests
import json
import csv

resp = requests.get("https://reqres.in/api/users")
print(resp)
resp_dict = resp.json()
pretty = json.dumps(resp.json(), indent=4)
print(pretty)

df = pd.DataFrame(resp_dict.get("data"))
print(df)
