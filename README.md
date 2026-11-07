# Assignment 4. Patient Zero

**Student ID:** 252357  
**Name:** Ismail Bolat  
**Group:** SE-2512

This project implements the five algorithm missions from Assignment 4. The explanations below are drafts to review and rewrite in your own words before submission, as required by the assignment.

## Program output

Run with `java -cp out Main`:

```text
Mutations: 65
Shared core: 1013
Marker hits: 6
First hit: 8376
Border length: 4
Shared words: 502
Tests needed: 41
Cheapest testing: 185
Longest chain: 32
```

My outbreak report: open `output/outbreak.html` in a browser. After opening it locally, take a screenshot of the page and save it as `output/outbreak.png` (the assignment requires the screenshot in the repository).

## Questions — draft answers to review and rewrite

**Mission 1.** `d[i][j]` is the minimum number of edits needed to change the first `i` characters of A into the first `j` characters of B. Each cell takes the minimum of replacing/matching the last characters, deleting a character, or inserting a character; the first row and column represent changing to or from an empty string.

**Mission 2.** A subsequence keeps the same order but may skip characters, while a substring must be continuous. Therefore, the shared core can be longer than the number of characters that match at exactly the same positions: matching characters can be separated by other characters in either strain.

**Mission 3.** The prefix function stores, for each prefix ending at a position, the length of its longest proper prefix that is also a suffix. KMP uses that information after a mismatch, so it shifts the pattern without moving the text pointer backwards. The marker's border length for this ID is **4**, meaning its longest proper prefix that is also a suffix has four characters.

**Mission 4.** Equal hashes do not guarantee equal strings because different windows can have the same hash (a collision), so the characters must be checked before counting a match. If many windows share a hash, more character-by-character comparisons are needed and the worst-case running time can grow to O((|A| + |B|) * k).

**Mission 5.** For each person there are two DP states: the minimum cost if that person is tested, and the minimum cost if that person is not tested. If a person is not tested, every child must be tested; if the person is tested, each child can use whichever state is cheaper. A greedy choice based only on the number of neighbours ignores the costs and the effect on neighbouring choices, so it can miss the minimum total cost.

## AI use

ChatGPT was used to explain the assignment, help implement and review the algorithm methods, and help prepare these README explanations. I will review the implementation and rewrite the explanations in my own words so I can explain every part during the defense.

## Timing (Mission 7)

Milliseconds, average of 3 runs from `java -cp out Bench` in the preparation environment. Timings vary by computer and JVM.

| test | size | time (ms) |
|---|---:|---:|
| naive search | 20 000 | 31.4 |
| naive search | 40 000 | 77.3 |
| naive search | 80 000 | 153.1 |
| KMP | 20 000 | 3.0 |
| KMP | 40 000 | 2.2 |
| KMP | 80 000 | 0.5 |
| edit distance | 500 | 8.4 |
| edit distance | 1 000 | 10.4 |
| edit distance | 2 000 | 26.2 |
| edit distance | 4 000 | 113.9 |

The naive search becomes slower as the text grows, while KMP is linear in the text and pattern lengths; the measured KMP values are small, so JVM and measurement noise make the doubling trend unclear. Edit distance uses a two-dimensional table, so doubling both string lengths makes the number of cells about four times larger; the measurements broadly show this quadratic growth, with some timing noise.

## Complexity

Let `n` and `m` be the lengths of the two strains, `t` the genome length, `p` the marker length, `k` the fixed window length (12), and `v` the number of people.

| algorithm | time | space | assumptions |
|---|---|---|---|
| Edit distance | O(nm) | O(nm) | Full DP table |
| LCS | O(nm) | O(nm) | Full DP table |
| Prefix function and KMP | O(p + t + h) | O(p + h) | `h` is the number of matches stored in the result |
| Rabin-Karp (shared words) | Expected O(n + m + k); worst O((n + m)k) | O(m) | Rolling hash; verifies characters for equal hashes; `k` is fixed at 12 in Main |
| Tree DP | O(v) | O(v) | Tree has `v - 1` edges; iterative traversal and reconstruction |

## Checks performed

- `java tools/Check.java` — tiny, small and dense examples all passed (all 9 output lines in each example).
- `javac -encoding UTF-8 -d out src/*.java` — compilation successful.
- `java -cp out Main` — generated the nine output lines above and `output/outbreak.html`.
- `java -cp out Bench` — timing results recorded above.

## Before submitting

- [ ] Open `output/outbreak.html` and save a screenshot as `output/outbreak.png`.
- [ ] Review and rewrite the draft answers above in your own words.
- [ ] Read and understand every method; be ready to explain it in the defense.
- [ ] Push the project to a public GitHub repository on `main`, with at least one meaningful commit after each mission.
- [ ] Submit the repository link in Moodle.
