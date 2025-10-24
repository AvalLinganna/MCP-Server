export const gitToolSchemas = [
  {
    name: 'git_add',
    description: 'Add file contents to the index (git add <pathspec>)',
    inputSchema: {
      type: 'object',
      properties: {
        files: {
          type: 'array',
          items: { type: 'string' },
          description: 'Array of files to add. If omitted, all files are added.'
        },
        directory: {
          type: 'string',
          description: 'The directory to run git add in',
          default: '.'
        }
      }
    }
  },
  {
    name: 'git_commit',
    description: 'Record changes to the repository (git commit -m <message> [files...])',
    inputSchema: {
      type: 'object',
      properties: {
        message: {
          type: 'string',
          description: 'The commit message'
        },
        files: {
          type: 'array',
          items: { type: 'string' },
          description: 'Array of files to commit. If omitted, all staged changes are committed.'
        },
        directory: {
          type: 'string',
          description: 'The directory to run git commit in',
          default: '.'
        }
      },
      required: ['message']
    }
  },
  {
    name: 'git_merge',
    description: 'Join two or more development histories together (git merge <branch>)',
    inputSchema: {
      type: 'object',
      properties: {
        branch: {
          type: 'string',
          description: 'The branch to merge into the current branch'
        },
        directory: {
          type: 'string',
          description: 'The directory to run git merge in',
          default: '.'
        }
      },
      required: ['branch']
    }
  },
  {
    name: 'git_pull',
    description: 'Fetch from and integrate with another repository or a local branch (git pull)',
    inputSchema: {
      type: 'object',
      properties: {
        directory: {
          type: 'string',
          description: 'The directory to run git pull in',
          default: '.'
        }
      }
    }
  },
  {
    name: 'git_push',
    description: 'Update remote refs along with associated objects (git push)',
    inputSchema: {
      type: 'object',
      properties: {
        directory: {
          type: 'string',
          description: 'The directory to run git push in',
          default: '.'
        }
      }
    }
  },
  {
    name: 'git_status',
    description: 'Show the working tree status (git status)',
    inputSchema: {
      type: 'object',
      properties: {
        directory: {
          type: 'string',
          description: 'The directory to run git status in',
          default: '.'
        }
      }
    }
  },
  {
    name: 'git_checkout',
    description: 'Switch branches or restore working tree files (git checkout <branch>)',
    inputSchema: {
      type: 'object',
      properties: {
        branch: {
          type: 'string',
          description: 'The branch to checkout.'
        },
        directory: {
          type: 'string',
          description: 'The directory to run git checkout in',
          default: '.'
        }
      },
      required: ['branch']
    }
  },
  {
    name: 'git_log',
    description: 'Show commit logs (git log --oneline)',
    inputSchema: {
      type: 'object',
      properties: {
        directory: {
          type: 'string',
          description: 'The directory to run git log in',
          default: '.'
        }
      }
    }
  },
  {
    name: 'git_diff',
    description: 'Show changes between commits (git diff)',
    inputSchema: {
      type: 'object',
      properties: {
        commit: {
          type: 'string',
          description: 'Optional commit to compare against HEAD.'
        },
        directory: {
          type: 'string',
          description: 'The directory to run git diff in',
          default: '.'
        }
      }
    }
  }
];
