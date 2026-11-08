# Draws the plots from results/results.csv
# Run: python3 plot.py   (needs matplotlib)
import csv
import matplotlib
matplotlib.use("Agg")
import matplotlib.pyplot as plt

rows = []
with open("results/results.csv") as f:
    for r in csv.DictReader(f):
        r["n"] = int(r["n"])
        r["time_ms"] = float(r["time_ms"])
        r["operations"] = int(r["operations"])
        r["memory_cells"] = int(r["memory_cells"])
        rows.append(r)


def get(family, algorithm, input_type):
    # returns (list of n, list of rows) sorted by n
    found = [r for r in rows if r["family"] == family and r["algorithm"] == algorithm and r["input"] == input_type]
    found.sort(key=lambda r: r["n"])
    return [r["n"] for r in found], found


def finish(fig, ax, title, xlabel, ylabel, takeaway, filename):
    ax.set_title(title)
    ax.set_xlabel(xlabel)
    ax.set_ylabel(ylabel)
    ax.grid(True, alpha=0.3)
    ax.legend()
    fig.text(0.5, 0.01, takeaway, ha="center", fontsize=9, wrap=True)
    fig.tight_layout(rect=(0, 0.06, 1, 1))
    fig.savefig("results/plots/" + filename, dpi=150)
    plt.close(fig)


# plot 1: recursive calls (log y)
fig, ax = plt.subplots(figsize=(7, 5))
for inp in ["disjoint", "random", "identical"]:
    ns, rs = get("edit_distance", "recursive", inp)
    ax.plot(ns, [r["operations"] for r in rs], marker="o", label="recursive, " + inp)
ns, rs = get("edit_distance", "table", "disjoint")
small = [r for r in rs if r["n"] <= 14]
ax.plot([r["n"] for r in small], [r["operations"] for r in small], marker="s", linestyle="--", label="table (cells), disjoint")
ax.set_yscale("log")
finish(fig, ax, "Recursive edit distance: number of calls",
       "string length n (chars)", "calls / cells computed (log scale)",
       "Takeaway: on disjoint strings the calls grow about 5.6x per extra char, while the table only grows as n*n.",
       "plot1_recursive_calls.png")

# plot 2: DP time, table vs two rows (random input)
fig, ax = plt.subplots(figsize=(7, 5))
for algo in ["table", "two_rows"]:
    ns, rs = get("edit_distance", algo, "random")
    big = [r for r in rs if r["n"] >= 250]
    ax.plot([r["n"] for r in big], [r["time_ms"] for r in big], marker="o", label=algo)
finish(fig, ax, "Edit distance: table vs two rows, random strings",
       "string length n (chars)", "median time (ms)",
       "Takeaway: both roughly follow n*n (timings are noisy), two rows is as fast as the table or faster on random input.",
       "plot2_dp_time.png")

# plot 3: memory cells (log-log)
fig, ax = plt.subplots(figsize=(7, 5))
for algo in ["table", "two_rows"]:
    ns, rs = get("edit_distance", algo, "random")
    big = [r for r in rs if r["n"] >= 250]
    ax.plot([r["n"] for r in big], [r["memory_cells"] for r in big], marker="o", label=algo)
ax.set_xscale("log")
ax.set_yscale("log")
finish(fig, ax, "Edit distance: allocated int cells",
       "string length n (chars, log scale)", "allocated int cells (log scale)",
       "Takeaway: the table needs about n*n cells, two rows only about 2n, so the gap grows with n.",
       "plot3_memory.png")

# plot 4: comparisons per text char at the biggest n (bars, log y)
# (lines overlapped a lot, so I use bars for the biggest text only)
fig, ax = plt.subplots(figsize=(8, 5))
inputs = ["random26", "random2", "all_match", "worst_naive"]
biggest = max(r["n"] for r in rows if r["family"] == "search")
width = 0.35
for k, algo in enumerate(["naive", "kmp"]):
    values = []
    for inp in inputs:
        r = [x for x in rows if x["family"] == "search" and x["algorithm"] == algo and x["input"] == inp and x["n"] == biggest][0]
        values.append(r["operations"] / r["n"])
    positions = [i + (k - 0.5) * width for i in range(len(inputs))]
    ax.bar(positions, values, width, label=algo)
    for p, v in zip(positions, values):
        ax.text(p, v * 1.1, "%.2f" % v, ha="center", fontsize=8)
ax.set_xticks(range(len(inputs)))
ax.set_xticklabels(inputs)
ax.set_yscale("log")
ax.set_ylim(0.5, 400)
finish(fig, ax, "String search: character comparisons per text char (n = %d, pattern length 100)" % biggest,
       "input type", "comparisons / n (ratio, log scale)",
       "Takeaway: KMP never needs more than 2 comparisons per char, naive needs 100 on the a...a inputs.",
       "plot4_comparisons_ratio.png")

# plot 5: search time on the worst input for naive
fig, ax = plt.subplots(figsize=(7, 5))
for algo in ["naive", "kmp"]:
    ns, rs = get("search", algo, "worst_naive")
    ax.plot(ns, [r["time_ms"] for r in rs], marker="o", label=algo)
ax.set_yscale("log")
ax.set_xscale("log")
finish(fig, ax, "String search time on the worst input for naive (text a...a, pattern a...ab)",
       "text length n (chars, log scale)", "median time (ms, log scale)",
       "Takeaway: both are linear in n, but naive is about 50x slower because it does about 100 comparisons per char.",
       "plot5_search_time.png")
print("plots saved")
