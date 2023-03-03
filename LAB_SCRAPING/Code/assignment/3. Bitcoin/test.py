import pandas as pd

# A = inputs(sig_id, output_id)-outputs(pk_id, id)
# transaction(id)-A(tx_id)

transactions = pd.read_csv(
    "./datasets/transactions.csv", names=["tx_id", "blk_id"])
inputs = pd.read_csv("./datasets/inputs.csv",
                     names=["in_id", "tx_id", "sig_id", "output_id"])
outputs = pd.read_csv("./datasets/outputs.csv",
                      names=["output_id", "tx_id", "pk_id", "value"])
dates = pd.read_csv("./datasets/dates.csv", date_parser="time")

dataset = transactions.merge(inputs, "inner", on="tx_id").merge(outputs,
                                                                "outer", left_on="sig_id", right_on="pk_id")

print(dataset)
