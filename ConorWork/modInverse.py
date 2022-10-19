firstNumber = 11
secondNumber = 7

difference = 3

combination = 10

i = 1

sevenMultiple = 1
nineMultiple=1

while i < combination:
    while sevenMultiple < 100:
        if (sevenMultiple * firstNumber) - (nineMultiple * secondNumber) == difference:
            print(sevenMultiple, " x ", firstNumber, "-", nineMultiple, " x ", secondNumber)
            break
        if (nineMultiple * secondNumber) - (sevenMultiple * firstNumber) == difference:
            print(nineMultiple, " x ", secondNumber, "-",sevenMultiple, " x ", firstNumber)
            break
        sevenMultiple = sevenMultiple + 1
    sevenMultiple=1
    nineMultiple = nineMultiple + 1
    i = i + 1