import React from 'react';
import { DropdownItem } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { Translate, translate } from 'react-jhipster';
import { NavLink as Link } from 'react-router-dom';
import { NavDropdown } from '../header-components';

export const EntitiesMenu = props => (
  // tslint:disable-next-line:jsx-self-close
  <NavDropdown icon="cogs" name={translate('global.menu.entities.main')} id="entity-menu">
    <DropdownItem tag={Link} to="/entity/controller">
      <FontAwesomeIcon icon="desktop" fixedWidth />&nbsp;<Translate contentKey="global.menu.entities.controller" />
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/pin">
      <FontAwesomeIcon icon="microchip" fixedWidth />&nbsp;<Translate contentKey="global.menu.entities.pin" />
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/sensor">
      <FontAwesomeIcon icon="broadcast-tower" fixedWidth />&nbsp;<Translate contentKey="global.menu.entities.sensor" />
    </DropdownItem>
    {/*
    <DropdownItem tag={Link} to="/entity/temperature">
      <FontAwesomeIcon icon="asterisk" fixedWidth />&nbsp;<Translate contentKey="global.menu.entities.temperature" />
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/humidity">
      <FontAwesomeIcon icon="asterisk" fixedWidth />&nbsp;<Translate contentKey="global.menu.entities.humidity" />
    </DropdownItem>
    */}
    <DropdownItem tag={Link} to="/entity/timer">
      <FontAwesomeIcon icon="clock" fixedWidth />&nbsp;<Translate contentKey="global.menu.entities.timer" />
    </DropdownItem>
    {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
  </NavDropdown>
);
