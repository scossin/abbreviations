class Sense:
    def __init__(self, short: str, ident: int):
        self.ident = ident
        self.short = short
        self.pref_label = ''
        self.alt_labels = []

    def add_pref_label(self, pref_label: str):
        self.__check_pref_label_not_set(pref_label)
        self.pref_label = pref_label

    def add_alt_label(self, alt_label: str):
        if alt_label not in self.alt_labels:
            self.alt_labels.append(alt_label)

    def __check_pref_label_not_set(self, pref_label: str):
        if self.pref_label != '':
            raise ValueError(f"{pref_label} of {self.short} was already set to {self.pref_label}")

    def check_pref_label_was_set(self):
        if self.pref_label == '':
            raise ValueError(f"pref_label of {self.short} was not set")

    def get_dict_rep(self) -> dict:
        return {
            "ident": self.ident,
            "pref_label": self.pref_label,
            "alt_labels": self.alt_labels
        }
