# Problem A, UVA 1709 - Amalgamated Artichokes

## Background

Fatima Cynara is an analyst at Amalgamated Artichokes (AA). As with any company, AA has had some very good times as well as some bad ones. Fatima does trending analysis of the stock prices for AA, and she wants to determine the largest decline in stock prices over various time spans. For example, if over a span of time the stock prices were 19, 12, 13, 11, 20 and 14, then the largest decline would be 8 between the first and fourth price. If the last price had been 10 instead of 14, then the largest decline would have been 10 between the last two prices. 

Fatima has done some previous analyses and has found that the stock price over any period of time can be modelled reasonably accurately with the following equation:

price(x) = p · (sin(a · x + b) + cos(c · x + d) + 2)

where p, a, b, c, and d are constants.Fatima would like you to write a program to de-
termine the largest price decline over a given sequence of prices. You have to consider
the prices only for integer values of x. 

## Input

The input file contains several test cases. Each test case is on a single line con-
taining 6 integers, p (1 ≤ p ≤ 1000), a, b, c, d (0 ≤ a, b, c, d ≤ 1000), and n (1 ≤
n ≤ 10 6 ). The first 5 integers are described above. The sequence of stock prices to consider are price(1), price(2), ..., price(n).

## Output

For each test case, display the maximum decline in stock prices. If there is no
decline, display the number ’0’. Your output should have an absolute or relative error of at most 10 ^-6^ .

## Sample Input

<p> 42 1 23 4 8 10 </p>
<p> 100 7 615 998 801 3 </p>
<p> 100 432 406 867 60 1000 </p>

## Sample Output

<p> 104.855110477 </p>
<p> 0.00 </p>
<p> 399.303813 </p>
