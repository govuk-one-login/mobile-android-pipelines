# mobile-android-pipelines
Reusable scripts and pipelines for building, testing and reporting on Android repositories.


The workflow can be called from GitHub actions as a step:

```yaml
steps:
  - name: Run Android workflow
    uses: govuk-one-login/mobile-android-pipelines@main
```

If used as part of a pull request workflow, the pipeline will:
- Checkout the code (both scripts and repository)
- Perform relevant setup of the GitHub runner
- Run static code analysis (such as linting)
- Run unit & instrumentation tests
- Upload results to SonarCloud
- Ensure that SonarCloud results pass to required level

If the GitHub Action was triggered by a different action, such as a merge or workflow dispatch, then the workflow will also:
- Determine the new semantic version number using conventional commits
- Tag the branch as a new release
- Build a new release artefact
- Upload the new release to GitHub Packages

## Updating gradle-wrapper

Gradle secure hash algorithm (SHA) pinning is in place through the `distributionSha256Sum` attribute in gradle-wrapper.properties. This means the gradle-wrapper must upgrade through the `./gradlew wrapper` command.
```
distributionBase=GRADLE_USER_HOME
distributionPath=wrapper/dists
distributionSha256Sum=2db75c40782f5e8ba1fc278a5574bab070adccb2d21ca5a6e5ed840888448046
distributionUrl=https\://services.gradle.org/distributions/gradle-8.10.2-bin.zip
networkTimeout=10000
validateDistributionUrl=true
 ```

Use the following command to update the gradle wrapper. Run the same command twice, [reason](https://sp4ghetticode.medium.com/the-elephant-in-the-room-how-to-update-gradle-in-your-android-project-correctly-09154fe3d47b).

```bash
./gradlew wrapper --gradle-version=8.10.2 --distribution-type=bin --gradle-distribution-sha256-sum=31c55713e40233a8303827ceb42ca48a47267a0ad4bab9177123121e71524c26
```

Flags:
- `gradle-version` self explanatory
- `distribution-type` set to `bin` short for binary refers to the gradle bin, often in this format `gradle-8.10.2-bin.zip`
- `gradle-distribution-sha256-sum` the SHA 256 checksum from this [page](https://gradle.org/release-checksums/), pick the binary checksum for the version used

The gradle wrapper update can include:
- gradle-wrapper.jar
- gradle-wrapper.properties
- gradlew
- gradlew.bat

You can use the following command to check the SHA 256 checksum of a file

```bash
shasum -a 256 gradle-8.10.2-bin.zip
```
## Hotfix process

There are GitHub actions in the actions folder to facilitate quick fix (hotfix) releases.
The process is as follows:
- Create a branch - temp/hotfix - off the release version/ commit required the fix is added to
- Create a hotfix/M.m.p (e.g. hotfix/1.0.0) branch off the temp/hotfix branch created above
- Add the fix on the branch from step 2
- Push the hotfix/M.m.p to remote
- Create PR for that to be merged into temp/hotfix
- Run the required workflows and request PR review
- Once successful, merge PR into temp/hotfix
- Monitor if packages successfully published - the hotfix branch will be automatically deleted as part of the merge workflow
- Merge main (or the branch used for development/ most up-to-date) into hotfix/M.m.p
- Create a PR from the updated hotfix/M.m.p to be merged back into main

For a push to PR workflow, `actions/hotfix-pr` github action can be used. See `.github/workflows/on_hotfix_pull_request.yml` as an example
For a merge to temp hotfix workflow, `actions/hotfix-merge` github action can be used. See `.github/workflows/on_push_hotfix.yml` as an example
