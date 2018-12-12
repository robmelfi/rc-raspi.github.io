import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './temperature.reducer';
import { ITemperature } from 'app/shared/model/temperature.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ITemperatureDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class TemperatureDetail extends React.Component<ITemperatureDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { temperatureEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="rcraspiApp.temperature.detail.title">Temperature</Translate> [<b>{temperatureEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="value">
                <Translate contentKey="rcraspiApp.temperature.value">Value</Translate>
              </span>
            </dt>
            <dd>{temperatureEntity.value}</dd>
            <dt>
              <span id="timestamp">
                <Translate contentKey="rcraspiApp.temperature.timestamp">Timestamp</Translate>
              </span>
            </dt>
            <dd>
              <TextFormat value={temperatureEntity.timestamp} type="date" format={APP_DATE_FORMAT} />
            </dd>
          </dl>
          <Button tag={Link} to="/entity/temperature" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>&nbsp;
          <Button tag={Link} to={`/entity/temperature/${temperatureEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.edit">Edit</Translate>
            </span>
          </Button>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ temperature }: IRootState) => ({
  temperatureEntity: temperature.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(TemperatureDetail);
