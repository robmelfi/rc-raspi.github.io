import React from 'react';

import { Translate } from 'react-jhipster';
import { Col } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

const tempHumWidget = props =>
  <>
    <Col xs="5" className="shadow-sm p-2 m-2 text-center">
      <div className="mb-3">
        <Translate contentKey="rcraspiApp.humidity.detail.title">
          Humidity
        </Translate>
      </div>
      <div className="">{props.humidity ? props.humidity : '-'}<span>&nbsp;%&nbsp;</span><span><FontAwesomeIcon icon="tint" fixedWidth /></span></div>
    </Col>
    <Col xs="5" className="shadow-sm p-2 m-2 text-center">
      <div className="mb-3">
        <Translate contentKey="rcraspiApp.temperature.detail.title">
          Temperature
        </Translate>
      </div>
      <div className="">{props.temperature ? props.temperature : '-'}<span>&nbsp;C&#176;&nbsp;<FontAwesomeIcon icon="thermometer-half" fixedWidth /></span></div>
    </Col>
  </>;

export default tempHumWidget;
