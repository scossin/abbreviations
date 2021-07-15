from typing import Dict, List

from Abbreviation import Abbreviation


class AllAbbs:
    def __init__(self):
        self.abbreviations: Dict[str, Abbreviation] = {}

    def add_pref_label(self, short: str, ident: str, pref_label: str):
        abb: Abbreviation = self.__get_abbreviation(short)
        abb.add_pref_label(ident, pref_label)

    def add_alt_label(self, short: str, ident: str, alt_label: str):
        abb: Abbreviation = self.__get_abbreviation(short)
        abb.add_alt_label(ident, alt_label)

    def __get_abbreviation(self, short: str) -> Abbreviation:
        self.__create_new_abb_if_not_exists(short)
        return self.abbreviations[short]

    def __create_new_abb_if_not_exists(self, short: str) -> None:
        if short not in self.abbreviations:
            new_abb: Abbreviation = Abbreviation(short)
            self.abbreviations[short] = new_abb

    def get_abbs(self):
        abbs: List[Abbreviation] = []
        [abbs.append(abb) for abb in self.abbreviations.values()]
        sorted(abbs, key=lambda x: x.short)
        return abbs

