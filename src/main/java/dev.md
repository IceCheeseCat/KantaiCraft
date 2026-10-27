# Develop change logs

## 2026-10-11

### Expedition

A task that can be assigned to a ship to gain resources

### Expedition Efficiency
Efficiency depends on ship speed and class.

$$ C = A × speed × class, 
Eff = min(BASE_EFF + 1 / (1 + e^(-0.1 × C)), 1) $$

| Variable | Definition | Range / Type | Example |
|:--------:|:-----------|:------------:|:-------:|
| A | Speed/class coefficient | Real number | 1.2 |
| speed | Ship speed | Real number | 5.5 |
| class | Ship class factor | [0, 1] | 0.5 |
| C | Score calculated from speed and class | Real number | 3.3 |
| BASE_EFF | Base efficiency offset | Real number | 0.35 |
| Eff | Final efficiency | [0, 1] | 0.932 |


### Expedition Bonus

$$ Bonus = sum(item\_bonus) $$

| Name                         | Bonus Score | Equippable Ship                                                                        |
|:-----------------------------|:-----------:|:---------------------------------------------------------------------------------------|
| Drum Canister                |     0.1     | DD, DE, CL, CLT, CT, CA, CAV, FBB, BB, BBV, CVL, CV, CVB, SS, SSV, AV, LHA, AO, AS, AR |
| Daihatsu-class Landing Craft |     0.2     | DE, DD, CL, CLT, CA, CAV, FBB, BB, BBV, CVL, CV, CVB, SS, SSV, AV, LHA, AO, AS, AR     |
| Other Landing Craft          |    0.05     | DE, DD, CL, CLT, CA, CAV, FBB, BB, BBV, CVL, CV, CVB, SS, SSV, AV, LHA, AO, AS, AR                                                                                       |

## 2026-10-27

### Expedition Block

- A table similar to command center, a list of different expeditions to accept.
- Assign multiple ships to join an expedition.
