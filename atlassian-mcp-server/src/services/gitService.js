import { exec } from 'child_process';
import { promisify } from 'util';
const execAsync = promisify(exec);

export class GitService {
  static async gitAdd({ files, directory = '.' }) {
    const filesArg = files && files.length ? files.join(' ') : '.';
    const cmd = `git add ${filesArg}`;
    await execAsync(cmd, { cwd: directory });
    return { success: true, message: `Added files: ${filesArg}` };
  }

  static async gitCommit({ message, files, directory = '.' }) {
    const filesArg = files && files.length ? files.join(' ') : '';
    const cmd = `git commit -m "${message.replace(/"/g, '\"')}" ${filesArg}`;
    await execAsync(cmd, { cwd: directory });
    return { success: true, message: `Committed with message: ${message}` };
  }

  static async gitMerge({ branch, directory = '.' }) {
    const cmd = `git merge ${branch}`;
    await execAsync(cmd, { cwd: directory });
    return { success: true, message: `Merged branch: ${branch}` };
  }

  static async gitPull({ directory = '.' }) {
    const cmd = 'git pull';
    await execAsync(cmd, { cwd: directory });
    return { success: true, message: 'Pulled latest changes' };
  }

  static async gitPush({ directory = '.' }) {
    const cmd = 'git push';
    await execAsync(cmd, { cwd: directory });
    return { success: true, message: 'Pushed to remote' };
  }

  static async gitStatus({ directory = '.' }) {
    const cmd = 'git status';
    const { stdout } = await execAsync(cmd, { cwd: directory });
    return { success: true, status: stdout };
  }

  static async gitCheckout({ branch, directory = '.' }) {
    const cmd = `git checkout ${branch}`;
    await execAsync(cmd, { cwd: directory });
    return { success: true, message: `Checked out branch: ${branch}` };
  }

  static async gitLog({ directory = '.' }) {
    const cmd = 'git log --oneline';
    const { stdout } = await execAsync(cmd, { cwd: directory });
    return { success: true, log: stdout };
  }

  static async gitDiff({ commit, directory = '.' }) {
    const cmd = commit ? `git diff ${commit}` : 'git diff';
    const { stdout } = await execAsync(cmd, { cwd: directory });
    return { success: true, diff: stdout };
  }
}
