import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAllAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './controller.reducer';
import { IController } from 'app/shared/model/controller.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IControllerProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export class Controller extends React.Component<IControllerProps> {
  componentDidMount() {
    this.props.getEntities();
  }

  render() {
    const { controllerList, match } = this.props;
    return (
      <div>
        <h2 id="controller-heading">
          <Translate contentKey="rcraspiApp.controller.home.title">Controllers</Translate>
          <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
            <FontAwesomeIcon icon="plus" />&nbsp;
            <Translate contentKey="rcraspiApp.controller.home.createLabel">Create new Controller</Translate>
          </Link>
        </h2>
        <div className="table-responsive">
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="global.field.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="rcraspiApp.controller.name">Name</Translate>
                </th>
                <th>
                  <Translate contentKey="rcraspiApp.controller.mode">Mode</Translate>
                </th>
                <th>
                  <Translate contentKey="rcraspiApp.controller.pin">Pin</Translate>
                </th>
                <th>
                  <Translate contentKey="rcraspiApp.controller.status">Status</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {controllerList.map((controller, i) => (
                <tr key={`entity-${i}`}>
                  <td>
                    <Button tag={Link} to={`${match.url}/${controller.id}`} color="link" size="sm">
                      {controller.id}
                    </Button>
                  </td>
                  <td>{controller.name}</td>
                  <td>
                    <Translate contentKey={`rcraspiApp.IO.${controller.mode}`} />
                  </td>
                  <td>{controller.pinName ? <Link to={`pin/${controller.pinId}`}>{controller.pinName}</Link> : ''}</td>
                  <td>{controller.status ? 'High' : 'Low'}</td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${controller.id}/delete`} color="danger" size="sm">
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

const mapStateToProps = ({ controller }: IRootState) => ({
  controllerList: controller.entities
});

const mapDispatchToProps = {
  getEntities
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(Controller);
