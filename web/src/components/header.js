import AnimalEnrichmentTrackerClient from '../api/animalEnrichmentTrackerClient';
import BindingClass from "../util/bindingClass";

/**
 * The header component for the website.
 */
export default class Header extends BindingClass {
    constructor() {
        super();

        const methodsToBind = [
            'addHeaderToPage', 'createSiteTitle', 'createMenuHeader', 'createUserInfoForHeader',
            'createLoginButton', 'createLoginButton', 'createLogoutButton'
        ];
        this.bindClassMethods(methodsToBind, this);

        this.client = new AnimalEnrichmentTrackerClient();
    }

    /**
     * Add the header to the page.
     */
    async addHeaderToPage() {
        const currentUser = await this.client.getIdentity();

        const siteTitle = this.createSiteTitle();
        const menuHeader = this.createMenuHeader();
        const userInfo = this.createUserInfoForHeader(currentUser);

        const header = document.getElementById('header');
        header.appendChild(siteTitle);
        header.appendChild(menuHeader);
        header.appendChild(userInfo);
    }

    createSiteTitle() {
        const homeButton = document.createElement('a');
        homeButton.classList.add('header_home');
        homeButton.href = 'index.html';
        homeButton.innerText = 'SafariJoy';

        const siteTitle = document.createElement('div');
        siteTitle.classList.add('site-title');
        siteTitle.appendChild(homeButton);

        return siteTitle;
    }

    createMenuHeader() {
        const menuList = document.createElement('ul');
        menuList.classList.add('menu_list');

        menuList.appendChild(this.createViewHabitatsLink());
        menuList.appendChild(this.createViewEnrichmentActivitiesLink());

        const menu = document.createElement('div');
        menu.classList.add('menu');
        menu.appendChild(menuList);

        return menu;
    }

    createViewHabitatsLink() {
        const viewHabitatsLink = document.createElement('a');
        viewHabitatsLink.href = 'viewAllHabitats.html';
        const viewHabitats = document.createElement('li');
        viewHabitats.classList.add('nav');
        viewHabitats.innerText = 'Habitats';
        viewHabitatsLink.appendChild(viewHabitats);
        return viewHabitatsLink;
    }

    createViewEnrichmentActivitiesLink() {
        const viewEnrichmentActivitiesLink = document.createElement('a');
        viewEnrichmentActivitiesLink.href = 'viewAllActivities.html';
        const viewActivities = document.createElement('li');
        viewActivities.classList.add('nav');
        viewActivities.innerText = 'Enrichment Activities';
        viewEnrichmentActivitiesLink.appendChild(viewActivities);
        return viewEnrichmentActivitiesLink;
    }

    createUserInfoForHeader(currentUser) {
        const userInfo = document.createElement('div');
        userInfo.classList.add('user');

        const childContent = currentUser
            ? this.createLogoutButton(currentUser)
            : this.createLoginButton();

        userInfo.appendChild(childContent);

        return userInfo;
    }

    createLoginButton() {
        return this.createButton('Login', this.client.login);
    }

    createLogoutButton(currentUser) {
        return this.createButton(`Logout: ${currentUser.name}`, this.client.logout);
    }

    createButton(text, clickHandler) {
        const button = document.createElement('a');
        button.classList.add('button');
        button.href = '#';
        button.innerText = text;

        button.addEventListener('click', async () => {
            await clickHandler();
        });

        return button;
    }
}