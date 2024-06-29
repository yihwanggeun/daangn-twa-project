import React, { useState } from 'react';
import './navbar.css';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSearch, faBars } from '@fortawesome/free-solid-svg-icons';

function Navbar() {
    const [active, setActive] = useState('nav__menu');

    const navToggle = () => {
        setActive(active === 'nav__menu' ? 'nav__menu nav__active' : 'nav__menu');
    };

    return (
        <div>
            <nav className="nav">
                <a href="#" className="nav__brand">
                    <img className="nav__logo" src="asset/logo.jpg" alt="Logo" />
                </a>
                <nav className={active}>
                    <ul className="nav__list">
                        <li className="nav__item"><a href="#" className="nav__link">중고거래</a></li>
                        <li className="nav__item"><a href="#" className="nav__link">동네업체</a></li>
                        <li className="nav__item"><a href="#" className="nav__link">알바</a></li>
                        <li className="nav__item"><a href="#" className="nav__link">부동산</a></li>
                        <li className="nav__item"><a href="#" className="nav__link">중고차 직거래</a></li>
                    </ul>
                </nav>
                <div className="nav__right">
                    <span className="navbar__search">
                        <FontAwesomeIcon icon={faSearch} />
                    </span>
                    <button className="navbar__chat" onClick={navToggle}>
                        <FontAwesomeIcon icon={faBars} />
                    </button>
                </div>
            </nav>
        </div>
    );
}

export default Navbar;
