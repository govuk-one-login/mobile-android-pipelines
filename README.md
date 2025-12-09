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

A gradle wrapper validation check is now in place in `.github/workflows/check-build-logic.yml`
