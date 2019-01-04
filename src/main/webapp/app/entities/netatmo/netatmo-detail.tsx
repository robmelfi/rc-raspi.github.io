import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './netatmo.reducer';
import { INetatmo } from 'app/shared/model/netatmo.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface INetatmoDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class NetatmoDetail extends React.Component<INetatmoDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { netatmoEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="rcraspiApp.netatmo.detail.title">Netatmo</Translate> [<b>{netatmoEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="clientId">
                <Translate contentKey="rcraspiApp.netatmo.clientId">Client Id</Translate>
              </span>
            </dt>
            <dd>{netatmoEntity.clientId}</dd>
            <dt>
              <span id="clientSecret">
                <Translate contentKey="rcraspiApp.netatmo.clientSecret">Client Secret</Translate>
              </span>
            </dt>
            <dd>{netatmoEntity.clientSecret}</dd>
            <dt>
              <span id="email">
                <Translate contentKey="rcraspiApp.netatmo.email">Email</Translate>
              </span>
            </dt>
            <dd>{netatmoEntity.email}</dd>
            <dt>
              <span id="password">
                <Translate contentKey="rcraspiApp.netatmo.password">Password</Translate>
              </span>
            </dt>
            <dd>*********</dd>
            <dt>
              <span id="enabled">
                <Translate contentKey="rcraspiApp.netatmo.enabled">Enabled</Translate>
              </span>
            </dt>
            <dd>{netatmoEntity.enabled ? 'true' : 'false'}</dd>
          </dl>
          <Button tag={Link} to="/entity/netatmo" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>&nbsp;
          <Button tag={Link} to={`/entity/netatmo/${netatmoEntity.id}/edit`} replace color="primary">
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

const mapStateToProps = ({ netatmo }: IRootState) => ({
  netatmoEntity: netatmo.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(NetatmoDetail);
