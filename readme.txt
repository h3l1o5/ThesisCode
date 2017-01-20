current version:

Combinatorial action game's winner determinate part is OK and payment algorithm is OK.
Now we can decide the game is multi-unit or not.

update:
[refactoring]
change package name "Games" to "Problems"
combine two priority attributes in the bidder class into one.
combine two payment attributes in the bidder class into one.
rewrite game_async method in the CA class.
rewrite calculateCriticalValue method in the CA class.
bidder class is no longer the subclass of player class now.
rewrite test file.
and do some little fixing of all other attributes/methods that has dependency of things mentioned above.



TODO:

1. 
finish the refactoring.
2. 
try all new ways to calculate the bidder's priority of ours winner determination.
see if the results will better then LOS02's method in the multi-unit environment.
3. 
repair centralized algo.
