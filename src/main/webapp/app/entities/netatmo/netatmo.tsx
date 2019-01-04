import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAllAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './netatmo.reducer';
import { INetatmo } from 'app/shared/model/netatmo.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface INetatmoProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export class Netatmo extends React.Component<INetatmoProps> {
  componentDidMount() {
    this.props.getEntities();
  }

  render() {
    const { netatmoList, match } = this.props;
    return (
      <div>
        <h2 id="netatmo-heading">
          <Translate contentKey="rcraspiApp.netatmo.home.title">Netatmo</Translate>
          {netatmoList.length === 0 && (
            <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
              <FontAwesomeIcon icon="plus" />
              &nbsp;
              <Translate contentKey="rcraspiApp.netatmo.home.createLabel">Create new Netatmo</Translate>
            </Link>
          )}
        </h2>
        <div className="table-responsive">
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="global.field.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="rcraspiApp.netatmo.clientId">Client Id</Translate>
                </th>
                <th>
                  <Translate contentKey="rcraspiApp.netatmo.clientSecret">Client Secret</Translate>
                </th>
                <th>
                  <Translate contentKey="rcraspiApp.netatmo.email">Email</Translate>
                </th>
                <th>
                  <Translate contentKey="rcraspiApp.netatmo.password">Password</Translate>
                </th>
                <th>
                  <Translate contentKey="rcraspiApp.netatmo.enabled">Enabled</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {netatmoList.map((netatmo, i) => (
                <tr key={`entity-${i}`}>
                  <td>
                    <Button tag={Link} to={`${match.url}/${netatmo.id}`} color="link" size="sm">
                      {netatmo.id}
                    </Button>
                  </td>
                  <td>{netatmo.clientId}</td>
                  <td>{netatmo.clientSecret}</td>
                  <td>{netatmo.email}</td>
                  <td>********</td>
                  <td>{netatmo.enabled ? <FontAwesomeIcon icon="check" /> : <FontAwesomeIcon icon="times" />}</td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${netatmo.id}`} color="info" size="sm">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${netatmo.id}/edit`} color="primary" size="sm">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${netatmo.id}/delete`} color="danger" size="sm">
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        </div>
      </div>
    );
  }
}

const mapStateToProps = ({ netatmo }: IRootState) => ({
  netatmoList: netatmo.entities
});

const mapDispatchToProps = {
  getEntities
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(Netatmo);
