# PawPals



## Background

I created PawPals as a member of a 4-person team for COM S 309, Distributed Development of Software, at Iowa State University. 

The class was structured to teach students best practices in software development in the most effective way: by doing it! The classwork consisted of developing the 
frontend and backend of a functional Android mobile app from scratch over the course of the semester.

## What is PawPals?

PawPals was conceived as "Tinder for dogs." PawPals is a way for pet enthusiasts to connect with each other to buy, adopt, or sell pets and find the right sitter for
their furry friend if the need arises. It adopts the intuitive swiping interface of dating apps like Tinder to connect people to the pets they would like to provide a home for 
or sitters they want to hire. 

## How does it work?

The basic design of PawPals is familiar to any mobile developer. Users have an account with at least one profile type. Profile types correspond to their role. 

- A buyer profile is able to swipe on sitters or pets and initiate a conversation to solicit their service.
- A seller profile lets the user put their pets up for sale or adoption.
- A sitter profile adds the user to the list of possible sitters.

The frontend guides the user through profile addition and subtraction, pet addition or deletion for sellers, changing account settings, a chat interface, 
and, of course, a filtered swiping interface.

The backend supports all of those features and stores the information on an Apache Linux server system. For the project, we used a VM hosted by Iowa State.
The frontend and backend are connected using the REST API.

## My Role

I worked as one of the two back end developers, a tester, and as an organizer.

## Development Tools

- PawPals was developed using Iowa State's GitLab system, including CI/CD tools.
- The backend was developed using IntelliJ IDE.
- The frontend development and app testing were developed in Android Studio.
- Postman was used to test the API before integration.
- The backend is based on the SpringBoot framework.

***

# Editing this README

When you're ready to make this README your own, just edit this file and use the handy template below (or feel free to structure it however you want - this is just a starting point!). Thank you to [makeareadme.com](https://www.makeareadme.com/) for this template.

## Suggestions for a good README
Every project is different, so consider which of these sections apply to yours. The sections used in the template are suggestions for most open source projects. Also keep in mind that while a README can be too long and detailed, too long is better than too short. If you think your README is too long, consider utilizing another form of documentation rather than cutting out information.

## Name
Choose a self-explaining name for your project.
- [ ] PawPals

## Description
Let people know what your project can do specifically. Provide context and add a link to any reference visitors might be unfamiliar with. A list of Features or a Background subsection can also be added here. If there are alternatives to your project, this is a good place to list differentiating factors.

## Badges
On some READMEs, you may see small images that convey metadata, such as whether or not all the tests are passing for the project. You can use Shields to add some to your README. Many services also have instructions for adding a badge.

## Visuals
Depending on what you are making, it can be a good idea to include screenshots or even a video (you'll frequently see GIFs rather than actual videos). Tools like ttygif can help, but check out Asciinema for a more sophisticated method.

## Installation
Within a particular ecosystem, there may be a common way of installing things, such as using Yarn, NuGet, or Homebrew. However, consider the possibility that whoever is reading your README is a novice and would like more guidance. Listing specific steps helps remove ambiguity and gets people to using your project as quickly as possible. If it only runs in a specific context like a particular programming language version or operating system or has dependencies that have to be installed manually, also add a Requirements subsection.

## Usage
Use examples liberally, and show the expected output if you can. It's helpful to have inline the smallest example of usage that you can demonstrate, while providing links to more sophisticated examples if they are too long to reasonably include in the README.

## Support
Tell people where they can go to for help. It can be any combination of an issue tracker, a chat room, an email address, etc.

## Roadmap
If you have ideas for releases in the future, it is a good idea to list them in the README.

## Contributing
State if you are open to contributions and what your requirements are for accepting them.

For people who want to make changes to your project, it's helpful to have some documentation on how to get started. Perhaps there is a script that they should run or some environment variables that they need to set. Make these steps explicit. These instructions could also be useful to your future self.

You can also document commands to lint the code or run tests. These steps help to ensure high code quality and reduce the likelihood that the changes inadvertently break something. Having instructions for running tests is especially helpful if it requires external setup, such as starting a Selenium server for testing in a browser.

## Authors and acknowledgment
Show your appreciation to those who have contributed to the project.

## License
For open source projects, say how it is licensed.

## Project status
If you have run out of energy or time for your project, put a note at the top of the README saying that development has slowed down or stopped completely. Someone may choose to fork your project or volunteer to step in as a maintainer or owner, allowing your project to keep going. You can also make an explicit request for maintainers.
