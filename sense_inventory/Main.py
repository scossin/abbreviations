import json
from typing import List

from Abbreviation import Abbreviation
from AllAbbs import AllAbbs


## Convert the TSV file to a JSON that checks pref and alternative labels are correctly set for every abbreviation

if __name__ == "__main__":
    allAbbs = AllAbbs()
    filename = "sense_inventory.tsv"
    with open(filename, "r") as f:
        next(f)  # header
        for line in f:
            short, label, ident, is_pref_label = line.split("\t")  # aa	air ambiant	1	1
            is_pref_label = is_pref_label.strip()
            if is_pref_label == '1':
                allAbbs.add_pref_label(short, ident, label)
            else:
                allAbbs.add_alt_label(short, ident, label)
    abbs: List[Abbreviation] = allAbbs.get_abbs()
    [sense.check_pref_label_was_set() for abb in abbs for sense in abb.get_senses()]
    data = [abb.get_dict_rep() for abb in abbs]
    with open('sense_inventory.json', 'w') as f:
        json.dump(data, f)
