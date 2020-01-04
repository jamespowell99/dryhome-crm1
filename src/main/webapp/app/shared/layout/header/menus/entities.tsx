import React from 'react';
import { DropdownItem } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { NavLink as Link } from 'react-router-dom';
import { NavDropdown } from '../header-components';

export const EntitiesMenu = props => (
  // tslint:disable-next-line:jsx-self-close
  <NavDropdown icon="th-list" name="Customers" id="entity-menu">
    <DropdownItem tag={Link} to="/entity/dampproofer">
      <FontAwesomeIcon icon="asterisk" fixedWidth />
      &nbsp;Damp Proofers
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/domestic">
      <FontAwesomeIcon icon="asterisk" fixedWidth />
      &nbsp;Domestics
    </DropdownItem>
    {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
  </NavDropdown>
);
