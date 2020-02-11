import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, ButtonDropdown, DropdownItem, DropdownMenu, DropdownToggle } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './manual-label.reducer';
import { IManualLabel } from 'app/shared/model/manual-label.model';
// tslint:disable-next-line:no-unused-variable
import { APP_LOCAL_DATETIME_FORMAT_DOC_GENERATION } from 'app/config/constants';
import { getDocument } from 'app/shared/reducers/doc-generation';
import print from 'print-js';
import moment from 'moment';

export interface IManualLabelDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IManualLabelDetailState {
  downloadDropdownOpen: boolean;
  printDropdownOpen: boolean;
}

export class ManualLabelDetail extends React.Component<IManualLabelDetailProps, IManualLabelDetailState> {
  constructor(props) {
    super(props);

    this.toggleDownload = this.toggleDownload.bind(this);
    this.togglePrint = this.togglePrint.bind(this);
    this.state = {
      downloadDropdownOpen: false,
      printDropdownOpen: false
    };
  }

  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  componentWillUpdate(nextProps, nextState) {
    if (nextProps.printDocumentBlob && nextProps.printDocumentBlob !== this.props.printDocumentBlob) {
      const reader = new FileReader();
      reader.onload = () => {
        const result = reader.result as string;
        const b64 = result.replace(/^data:.+;base64,/, '');
        print({ printable: b64, type: 'pdf', base64: true });
      };
      reader.readAsDataURL(new Blob([nextProps.printDocumentBlob]));
    } else if (nextProps.downloadDocumentBlob && nextProps.downloadDocumentBlob !== this.props.downloadDocumentBlob) {
      const url = window.URL.createObjectURL(new Blob([nextProps.downloadDocumentBlob]));
      const link = document.createElement('a');
      link.href = url;
      const currentDate = new Date();
      // todo get docName?
      link.setAttribute(
        'download',
        `${nextProps.manualLabelEntity.id}-manual-label-${moment().format(APP_LOCAL_DATETIME_FORMAT_DOC_GENERATION)}.docx`
      );
      document.body.appendChild(link);
      link.click();
    }
  }

  callDownload = event => {
    this.props.getDocument('manual-labels', event.target.id, this.props.manualLabelEntity.id, 'DOCX');
  };

  callPrint = event => {
    this.props.getDocument('manual-labels', event.target.id, this.props.manualLabelEntity.id, 'PDF');
  };

  toggleDownload() {
    this.setState({
      downloadDropdownOpen: !this.state.downloadDropdownOpen
    });
  }

  togglePrint() {
    this.setState({
      printDropdownOpen: !this.state.printDropdownOpen
    });
  }

  render() {
    const { manualLabelEntity, generatingDocument } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            ManualLabel [<b>{manualLabelEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="line1">Line 1</span>
            </dt>
            <dd>{manualLabelEntity.line1}</dd>
            <dt>
              <span id="line2">Line 2</span>
            </dt>
            <dd>{manualLabelEntity.line2}</dd>
            <dt>
              <span id="line3">Line 3</span>
            </dt>
            <dd>{manualLabelEntity.line3}</dd>
            <dt>
              <span id="line4">Line 4</span>
            </dt>
            <dd>{manualLabelEntity.line4}</dd>
            <dt>
              <span id="line5">Line 5</span>
            </dt>
            <dd>{manualLabelEntity.line5}</dd>
            <dt>
              <span id="line6">Line 6</span>
            </dt>
            <dd>{manualLabelEntity.line6}</dd>
            <dt>
              <span id="line7">Line 7</span>
            </dt>
            <dd>{manualLabelEntity.line7}</dd>
          </dl>
          <Row>
            <Button tag={Link} to="/entity/manual-label" replace color="info">
              <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
            </Button>
            &nbsp;
            <Button tag={Link} to={`/entity/manual-label/${manualLabelEntity.id}/edit`} replace color="primary">
              <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
            </Button>
            &nbsp;
            <ButtonDropdown isOpen={this.state.downloadDropdownOpen} toggle={this.toggleDownload}>
              <DropdownToggle caret color="primary">
                Download
              </DropdownToggle>
              <DropdownMenu>
                <DropdownItem onClick={this.callDownload} id={'labels'}>
                  Labels
                </DropdownItem>
              </DropdownMenu>
            </ButtonDropdown>
            &nbsp;
            <ButtonDropdown isOpen={this.state.printDropdownOpen} toggle={this.togglePrint}>
              <DropdownToggle caret color="primary">
                Print
              </DropdownToggle>
              <DropdownMenu>
                <DropdownItem onClick={this.callPrint} id={'labels'}>
                  Labels
                </DropdownItem>
              </DropdownMenu>
            </ButtonDropdown>
          </Row>
          <Row>{generatingDocument ? <span>Generating...</span> : <span />}</Row>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ manualLabel, docGeneration }: IRootState) => ({
  manualLabelEntity: manualLabel.entity,
  generatingDocument: docGeneration.generatingDocument,
  downloadDocumentBlob: docGeneration.downloadDocumentBlob,
  printDocumentBlob: docGeneration.printDocumentBlob
});

const mapDispatchToProps = { getEntity, getDocument };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(ManualLabelDetail);
