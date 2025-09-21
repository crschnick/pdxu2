<p align="center">
    <a href="https://kickstartfx.xpipe.io/" target="_blank" rel="noopener">
        <img src="https://kickstartfx.xpipe.io/images/demo1.png" alt="XPipe Banner" />
    </a>
</p>

<h1></h1>

## About

KickstartFX is an advanced, ready-to-use template for JavaFX applications. It can serve as a solid foundation for your own JavaFX application as everything is fully customizable and extendable. You can find the documentation at [https://kickstartfx.xpipe.io](https://kickstartfx.xpipe.io).

KickstartFX is much more than just a basic template that opens a simple window. It contains a lot of code to handle the challenges of applications in the real world to achieve the best possible desktop application experience across all operating systems. The code is based on [XPipe](https://github.com/xpipe-io/kickstartfx), a well-established JavaFX application, and is the result of years of experience developing a desktop application that is used by many thousands of users right now.

It features the following features that you won't find in other templates:
- A fully up-to-date build using the latest features of JDK25, Gradle 9, JavaFX 25, WiX 6, and much more
- Native executable and installer generation for all operating systems using native tools
- A fully modularized build, including fully modularized dependencies and the usage of jmods
- [Leyden AOT cache](https://openjdk.org/projects/leyden/) generation logic and customizable training run implementations
- A ready-to-deploy GitHub actions pipeline to automatically build and release your application on all platforms
- Close-to-native theming capabilities with AtlantaFX themes as the basis combined with many manual improvements
- Advanced error handling and issue tracking with built-in support for Sentry
- Markdown rendering capabilities out-of-the-box with flexmark and the JavaFX WebView
- Integrated ability to automatically codesign the application on Windows and macOS
- Solid state management for caches, persistent data, and more
- Many common customization options available to users in a comprehensible settings menu
- Update check capabilities and notifications for new GitHub releases
- Built-in troubleshooting tools for developers and users, including debug mode, heap dump, and more
- Hot-reload capabilities for all resources, including reapplying stylesheets
- Plenty of checks to warn users about problems with their system configuration, environment, and compatibility
- Desktop and registry access support classes
- Robust dependency Linux package management and font handling, your application will even run in WSL
- Application instance management and coordination via inter-process communication
- System tray icon support and proper handling of AWT/Swing alongside JavaFX
- Built-in support for Jackson and Lombok
- Integrated translation support with user interface language changes applying instantly
- Self-restart functionality to spawn new independent processes of your application
- Application logo templates that look native on every operating system, including a macOS 26 liquid glass icon
- Included third-party open source licenses of all dependencies, plus the required button to display them in the application

## Status

KickstartFX is a brand-new project, at least technically. While most parts come straight from XPipe, there was still some work done adapting the code and build scripts to be more template-friendly for this new repository. While the original codebase from XPipe is very stable, there might be issues that were introduced in the template creation process when adapting the original codebase. These should be ironed out in a while. Make sure to report any issues that you find.

## Downloads

You can run the latest release right now to see for yourself. These releases were generated using this template repository.

### Windows

Installers are the easiest way to get started:

- [Windows .msi Installer (x86-64)](../../releases/latest/download/kickstartfx-installer-windows-x86_64.msi)
- [Windows .msi Installer (ARM 64)](../../releases/latest/download/kickstartfx-installer-windows-arm64.msi) (Not built all the time)

If you don't like installers, you can also use a portable version that is packaged as an archive:

- [Windows .zip Portable (x86-64)](../../releases/latest/download/kickstartfx-portable-windows-x86_64.zip)
- [Windows .zip Portable (ARM 64)](../../releases/latest/download/kickstartfx-portable-windows-arm64.zip) (Not built all the time)

### macOS

Installers are the easiest way to get started:

- [MacOS .pkg Installer (x86-64)](../../releases/latest/download/kickstartfx-installer-macos-x86_64.pkg)
- [MacOS .pkg Installer (ARM 64)](../../releases/latest/download/kickstartfx-installer-macos-arm64.pkg)

If you don't like installers, you can also use a portable version that is packaged as an archive:

- [MacOS .dmg Portable (x86-64)](../../releases/latest/download/kickstartfx-portable-macos-x86_64.dmg)
- [MacOS .dmg Portable (ARM 64)](../../releases/latest/download/kickstartfx-portable-macos-arm64.dmg)

### Linux

#### Debian-based distros

The following debian installers are available:

- [Linux .deb Installer (x86-64)](../../releases/latest/download/kickstartfx-installer-linux-x86_64.deb)
- [Linux .deb Installer (ARM 64)](../../releases/latest/download/kickstartfx-installer-linux-arm64.deb) (Not built all the time)

Note that you should use apt to install the package with `sudo apt install <file>` as other package managers, for example dpkg,
are not able to resolve and install any dependency packages.

#### RHEL-based distros

The following rpm installers are available:

- [Linux .rpm Installer (x86-64)](../../releases/latest/download/kickstartfx-installer-linux-x86_64.rpm)
- [Linux .rpm Installer (ARM 64)](../../releases/latest/download/kickstartfx-installer-linux-arm64.rpm) (Not built all the time)

The same applies here, you should use a package manager that supports resolving and installing required dependencies if needed.

#### Portable

In case you prefer to use an archive version that you can extract anywhere, you can use these:

- [Linux .tar.gz Portable (x86-64)](../../releases/latest/download/kickstartfx-portable-linux-x86_64.tar.gz)
- [Linux .tar.gz Portable (ARM 64)](../../releases/latest/download/kickstartfx-portable-linux-arm64.tar.gz) (Not built all the time)

Note that the portable version assumes that you have some basic packages for graphical systems already installed
as it is not a perfect standalone version. It should however run on most systems.

## Showcase

![Demo2](https://kickstartfx.xpipe.io/images/demo2.png)

![Demo3](https://kickstartfx.xpipe.io/images/demo3.png)

![Demo4](https://kickstartfx.xpipe.io/images/demo4.png)

## License

The source code of this repository is multi-licensed. It is under a strong copyleft GPL license by default but can also be licensed under a permissive Apache license upon request.

### Copyleft community license

By default, the source code in this repository is licensed under the [GNU General Public License](/LICENSE-GPL.md).

### Permissive community license

If this license is incompatible with your project, you can request to instead license the source code under the [Apache License](/LICENSE-APL.md) if you are creating an open source project with a publicly visible repository.

You can contact [crschnick@xpipe.io](mailto:crschnick@xpipe.io) to request this permissive license for your open source project free of charge. There are no strings attached with this. All dependencies that are included with KickstartFX are compatible with the Apache License as well. While there is no prioritized support with this, this repository will still see timely fixes and improvements for any issues that are reported.

### Permissive enterprise license

If this license is incompatible with your enterprise project, you can request to instead license the source code under the [Apache License](/LICENSE-APL.md). This license is best suited for individuals and organizations looking for key functionalities for application development while fully retaining their intellectual property rights. All dependencies that are included with KickstartFX are compatible with the Apache License as well.

You can contact [crschnick@xpipe.io](mailto:crschnick@xpipe.io) to get a quote for your use case of your enterprise. This can can optionally also include prioritized support.
