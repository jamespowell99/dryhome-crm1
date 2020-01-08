import React from 'react';

import { UncontrolledDropdown, DropdownToggle, DropdownMenu, NavItem, NavLink, NavbarBrand } from 'reactstrap';
import { NavLink as Link } from 'react-router-dom';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import appConfig from 'app/config/constants';

export const NavDropdown = props => (
  <UncontrolledDropdown nav inNavbar id={props.id}>
    <DropdownToggle nav caret className="d-flex align-items-center">
      <FontAwesomeIcon icon={props.icon} />
      <span>{props.name}</span>
    </DropdownToggle>
    <DropdownMenu right style={props.style}>
      {props.children}
    </DropdownMenu>
  </UncontrolledDropdown>
);

export const BrandIcon = props => (
  <div {...props} className="brand-icon">
    <img src="content/images/logo-jhipster.png" alt="Logo" />
  </div>
);

export const Brand = props => (
  <NavbarBrand tag={Link} to="/" className="brand-logo">
    <BrandIcon />
    <span className="brand-title">Dryhomecrm1</span>
    <span className="navbar-version">{appConfig.VERSION}</span>
  </NavbarBrand>
);

export const Home = props => (
  <NavItem>
    <NavLink tag={Link} to="/" className="d-flex align-items-center">
      <FontAwesomeIcon icon="home" />
      <span>Home</span>
    </NavLink>
  </NavItem>
);

export const Orders = props => (
  <NavItem>
    <NavLink tag={Link} to="/entity/customer-order" className="d-flex align-items-center">
      <span>Orders</span>
    </NavLink>
  </NavItem>
);

export const Products = props => (
  <NavItem>
    <NavLink tag={Link} to="/entity/product" className="d-flex align-items-center">
      <span>Products</span>
    </NavLink>
  </NavItem>
);

export const DampProofers = props => (
  <NavItem>
    <NavLink tag={Link} to="/entity/dampproofer" className="d-flex align-items-center">
      <span>Damp Proofers</span>
    </NavLink>
  </NavItem>
);

export const Domestics = props => (
  <NavItem>
    <NavLink tag={Link} to="/entity/domestic" className="d-flex align-items-center">
      <span>Domestics</span>
    </NavLink>
  </NavItem>
);
