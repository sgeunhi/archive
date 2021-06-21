#include "Backpack.h"
#include <iostream>
#include <malloc.h>

using namespace std;

Backpack::Backpack() {
    StoreInventory inventory;
    this->storeInventory = inventory.item_list;

    zones = new Item *[5];
    for (int i = 0; i < 3; i++) {
        zones[i] = new Item[1];
    }
    for (int i = 3; i < 5; i++) {
        zones[i] = new Item[2];
    }
    meals = new Meal();
    items = new Item();

    item_length = 0;
    meal_length = 0;
}

void Backpack::assignMeals(CustomerRequirement customerRequirement) {
    Weight mealWeight = customerRequirement.getPreferredMealWeight();
    DaysOnCamp day = customerRequirement.getDaysOnCamp();
    int d = 0;
    if (day == ONE) {
        d = 1;
    } else if (day == TWO) {
        d = 2;
    } else if (day == THREE) {
        d = 3;
    }
    meal_length = 4 * d - 2;
    meals = new Meal[meal_length];
    for (int i = 0; i < d; i++) {
        *(meals + 4 * i) = Meal(LUNCH, mealWeight);
        *(meals + 4 * i + 1) = Meal(SNACK, mealWeight);
    }
    if (d >= 2) {
        for (int i = 1; i < d; i++) {
            *(meals + 4 * i - 2) = Meal(BREAKFAST, mealWeight);
            *(meals + 4 * i - 1) = Meal(DINNER, mealWeight);
        }
    }
}

void Backpack::assignItem(CustomerRequirement customerRequirement) {
    Weight mealWeight = customerRequirement.getPreferredMealWeight();
    Weight itemWeight = customerRequirement.getPreferredItemWeight();
    DaysOnCamp day = customerRequirement.getDaysOnCamp();

    if (day == ONE) {
        item_length = 4;
    } else if (day == TWO || day == THREE) {
        if (mealWeight == HIGH) {
            item_length = 7;
        } else {
            item_length = 6;
        }
    }
    items = new Item[item_length];
    Item *requiredItem = new Item[item_length];
    ItemType itemTempList[7] = {LURE, FISHING_ROD, CLOTHING, WATER, SLEEPING_BAG, TENT, COOKING};
    for (int i = 0; i < item_length; i++) {
        if (itemTempList[i] == WATER) {
            *(requiredItem + i) = Item(itemTempList[i], HIGH);
        } else if (itemTempList[i] == SLEEPING_BAG) {
            *(requiredItem + i) = Item(itemTempList[i], MEDIUM);
        } else {
            *(requiredItem + i) = Item(itemTempList[i], itemWeight);
        }
    }
    for (int j = 0; j < item_length; j++) {
        for (int k = 0; k < 42; k++) {
            if (storeInventory[k].equals(requiredItem[j])) {
                *(items + j) = requiredItem[j];
                break;
            }
        }
    }
}

void Backpack::packBackpack() {
    for (int i = 0; i < item_length; i++) {
        switch (items[i].getItemType()) {
            case LURE:
                zones[0][0] = items[i];
                break;
            case FISHING_ROD:
                zones[1][0] = items[i];
                break;
            case CLOTHING:
                zones[2][0] = items[i];
                break;
            case COOKING:
                zones[3][0] = items[i];
                break;
            case WATER:
                zones[3][1] = items[i];
                break;
            case SLEEPING_BAG:
                zones[4][0] = items[i];
                break;
            case TENT:
                zones[4][1] = items[i];
                break;
        }
    }
}

void Backpack::addItem(Item item) {
    if (item_length != 0) {
        Item *itemsCopy = new Item[item_length];
        for (int i = 0; i < item_length; i++) {
            *(itemsCopy + i) = items[i];
//            itemsCopy[i] = items[i];
        }
        items = new Item[item_length + 1];
        for (int j = 0; j < item_length; j++) {
            *(items + j) = itemsCopy[j];
//            items[j] = itemsCopy[j];
        }
        items[item_length] = item;
        item_length++;
    } else {
        items = new Item[1];
        items[0] = item;
        item_length++;
    }
}

void Backpack::removeItem(int i) {
    if (item_length != 0) {
        Item *itemsCopy = new Item[item_length - 1];
        for (int k = 0; k < i; k++) {
            itemsCopy[k] = items[k];
        }
        for (int j = i + 1; j < item_length; j++) {
            itemsCopy[j - 1] = items[j];
        }
        items = new Item[item_length - 1];
        item_length--;
        for (int l = 0; l < item_length; l++) {
            items[l] = itemsCopy[l];
        }
    }
}

void Backpack::removeItem(Item item) {
    if (item_length != 0) {
        for (int i = 0; i < item_length; i++) {
            if (items[i].equals(item)) {
                removeItem(i);
                break;
            }
        }
    }
}

void Backpack::print() {
    const char *it;
    const char *w;
    for (int i = 0; i < 3; i++) {
        if (zones[i][0].getItemType() == SLEEPING_BAG) {
            it = "SLEEPING_BAG";
        } else if (zones[i][0].getItemType() == TENT) {
            it = "TENT";
        } else if (zones[i][0].getItemType() == LURE) {
            it = "LURE";
        } else if (zones[i][0].getItemType() == CLOTHING) {
            it = "CLOTHING";
        } else if (zones[i][0].getItemType() == FISHING_ROD) {
            it = "FISHING_ROD";
        } else if (zones[i][0].getItemType() == COOKING) {
            it = "COOKING";
        } else if (zones[i][0].getItemType() == WATER) {
            it = "WATER";
        }

        if (zones[i][0].getWeight() == LOW) {
            w = "LOW";
        } else if (zones[i][0].getWeight() == MEDIUM) {
            w = "MEDIUM";
        } else if (zones[i][0].getWeight() == HIGH) {
            w = "HIGH";
        }
        cout << "Zone " << i << ":" << endl;
        if (!zones[i][0].equals(Item(SLEEPING_BAG, LOW))) {
            cout << "\tItem{Item Type: " << it << ", Weight: " << w << "}" << endl;
        }
    }
    for (int j = 3; j < 5; j++) {
        cout << "Zone " << j << ":" << endl;
        for (int k = 0; k < 2; k++) {
            if (zones[j][k].getItemType() == SLEEPING_BAG) {
                it = "SLEEPING_BAG";
            } else if (zones[j][k].getItemType() == TENT) {
                it = "TENT";
            } else if (zones[j][k].getItemType() == LURE) {
                it = "LURE";
            } else if (zones[j][k].getItemType() == CLOTHING) {
                it = "CLOTHING";
            } else if (zones[j][k].getItemType() == FISHING_ROD) {
                it = "FISHING_ROD";
            } else if (zones[j][k].getItemType() == COOKING) {
                it = "COOKING";
            } else if (zones[j][k].getItemType() == WATER) {
                it = "WATER";
            }

            if (zones[j][k].getWeight() == LOW) {
                w = "LOW";
            } else if (zones[j][k].getWeight() == MEDIUM) {
                w = "MEDIUM";
            } else if (zones[j][k].getWeight() == HIGH) {
                w = "HIGH";
            }
            if (!zones[j][k].equals(Item(SLEEPING_BAG, LOW))) {
                cout << "\tItem{Item Type: " << it << ", Weight: " << w << "}" << endl;
            }
        }
    }
}

Meal *Backpack::getMeals() {
    return meals;
}

void Backpack::setMeals(Meal *m) {
    meals = m;
}

int Backpack::getMealLength() {
    return meal_length;
}

Item *Backpack::getItems() {
    return items;
}

void Backpack::setItems(Item *it) {
    items = it;
}

int Backpack::getItemLength() {
    return item_length;
}

Item **Backpack::getZones() {
    return zones;
}

void Backpack::setZones(Item **z) {
    zones = z;
}

Item *Backpack::getStoreInventory() {
    return storeInventory;
}

void Backpack::setStoreInventory(Item *s) {
    storeInventory = s;
}
