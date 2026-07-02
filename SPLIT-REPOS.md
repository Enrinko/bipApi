# Splitting client & server into separate repos (runbook)

Goal: **this repo stays the server**; the client (`cleintDONTDELETEFUCK/`) moves to its own
repository and is re-attached here as a **git submodule**.

> ⚠️ These steps are **not run automatically** — they rewrite git structure, create a new repo,
> and need a repository URL that only you can provide. Review, then run them yourself (or tell
> me the client repo URL and to go ahead, and I'll run them with you).

> **Peer note:** a submodule tightly couples the two repos (every clone needs
> `--recurse-submodules`, and the parent pins an exact client commit). Since the server and
> client are independent apps that don't build against each other, **two plain sibling repos**
> are usually simpler. The steps below do the submodule you asked for — just flagging the
> trade-off once.

## 0. Prerequisites

```bash
# Commit the devsecops scaffolding first, so the split starts from a clean tree:
git add -A
git commit -m "chore: add docker/compose/CI/observability + externalize secrets"
```

You also need the **client repo URL** (pick one):
- GitHub via CLI: nothing yet — `gh repo create` will make it (step 2).
- Existing empty remote: have the `git@github.com:<owner>/<name>.git` URL ready.

## Path A — fresh client repo (no history) — recommended for coursework

```bash
# 1. Copy the client OUT of this repo into a sibling directory
cd ..
cp -r bipApi/cleintDONTDELETEFUCK bip-client
cd bip-client

# 2. Init + first commit
printf 'target/\n.idea/\n*.iml\n*.iws\n*.ipr\n' > .gitignore
git init -b main
git add .
git commit -m "Import client from bipApi practice project"

# 3. Create the remote and push (needs GitHub CLI auth: `gh auth login`)
gh repo create <owner>/bip-client --private --source=. --remote=origin --push
#   …or with a URL you already have:
#   git remote add origin git@github.com:<owner>/bip-client.git && git push -u origin main
```

```bash
# 4. Back in the SERVER repo: drop the embedded client, add it as a submodule
cd ../bipApi
git rm -r cleintDONTDELETEFUCK
git commit -m "chore: remove client (moved to its own repo)"

git submodule add git@github.com:<owner>/bip-client.git client
git commit -m "chore: add client as submodule at ./client"
```

Result: `./client` is a submodule; clone with
`git clone --recurse-submodules <server-url>` (or `git submodule update --init` after cloning).

## Path B — preserve the client's git history

Use this if the commits touching `cleintDONTDELETEFUCK/` matter. Requires
[`git-filter-repo`](https://github.com/newren/git-filter-repo).

```bash
# 1. Work on a fresh clone so the original repo is untouched if something goes wrong
cd ..
git clone bipApi bip-client-extract
cd bip-client-extract

# 2. Keep only the client subdir, promoted to repo root, with its history
git filter-repo --subdirectory-filter cleintDONTDELETEFUCK

# 3. Point at the new remote and push
git remote add origin git@github.com:<owner>/bip-client.git
git push -u origin HEAD:main
```

Then do **step 4 from Path A** in the server repo (remove dir + add submodule).

## Rollback

Nothing here is destructive to the original until step 4 (`git rm`). If step 4 goes wrong before
you push:

```bash
git reset --hard HEAD~1   # undo the last commit (the git rm), restoring the client dir
```

The extracted `../bip-client` copy is independent — delete it freely if you restart.
