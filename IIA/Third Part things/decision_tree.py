import math


def entropy(pp, pm):
    card_S = pp + pm
    return (-(pp/card_S) * math.log2(pp/card_S)) - ((pm/card_S) * math.log2(pm/card_S))


def gain(pp, pm, *kwargs):
    if len(kwargs) % 2 == 0:
        e_S = entropy(pp, pm)
        card_S = pp + pm
        diff = 0
        for i in range(0, len(kwargs), 2):
            pp_v = kwargs[i]
            pm_v = kwargs[i+1]
            card_v = pp_v + pm_v
            diff += ((card_v / card_S) * entropy(pp_v, pm_v))
        return e_S - diff


print(entropy(4, 8))

# print(gain(4, 8, 2, 0, 2, 2, 0, 6))
