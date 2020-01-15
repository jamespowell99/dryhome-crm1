import React from 'react';
import { DropdownItem } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { NavLink as Link } from 'react-router-dom';
import { NavDropdown } from '../header-components';

export const ToolsMenu = props => (
  // tslint:disable-next-line:jsx-self-close
  <NavDropdown icon="th-list" name="Tools" id="entity-menu">
    <DropdownItem tag={Link} to="/entity/manual-invoice">
      &nbsp;Manual Invoice
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/manual-label">
      &nbsp;Manual Labels
    </DropdownItem>
  </NavDropdown>
);
