from typing import Dict, List

from Sense import Sense


class Abbreviation:
    def __init__(self, short: str):
        self.short = short
        self.senses: Dict[int, Sense] = {}

    def add_pref_label(self, ident: str, pref_label: str):
        sense: Sense = self.__get_sense(ident)
        sense.add_pref_label(pref_label)

    def add_alt_label(self, ident: str, alt_label: str):
        sense: Sense = self.__get_sense(ident)
        sense.add_alt_label(alt_label)

    def __get_sense(self, ident: str) -> Sense:
        ident_value = self.__convert_ident_to_num(ident)
        self.__create_new_sense_if_not_exists(ident_value)
        return self.senses[ident_value]

    def __create_new_sense_if_not_exists(self, ident_value: int) -> None:
        if ident_value not in self.senses:
            new_sense: Sense = Sense(self.short, ident_value)
            self.senses[ident_value] = new_sense

    @staticmethod
    def __convert_ident_to_num(ident: str) -> int:
        return int(ident)

    def get_senses(self) -> List[Sense]:
        senses: List[Sense] = []
        [senses.append(sense) for sense in self.senses.values()]
        sorted(senses, key=lambda x: x.ident)
        return senses

    def get_dict_rep(self) -> dict:
        return {
            "shortForm": self.short,
            "senses": [sense.get_dict_rep() for sense in self.get_senses()]
        }
