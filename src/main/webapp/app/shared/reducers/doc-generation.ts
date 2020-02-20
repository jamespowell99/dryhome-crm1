import axios from 'axios';

import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

export const ACTION_TYPES = {
  GET_DOCUMENT_DOCX: 'documents/GET_DOCUMENT_DOCX',
  GET_DOCUMENT_PDF: 'documents/GET_DOCUMENT_PDF'
};

const initialState = {
  errorMessage: null,
  generatingDocument: false,
  printDocumentBlob: null,
  downloadDocumentBlob: null
  //    todo field for tracking doc name?
};

export type DocGenerationState = Readonly<typeof initialState>;

export default (state: DocGenerationState = initialState, action): DocGenerationState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.GET_DOCUMENT_DOCX):
    case REQUEST(ACTION_TYPES.GET_DOCUMENT_PDF):
      return {
        ...state,
        generatingDocument: true,
        printDocumentBlob: null,
        downloadDocumentBlob: null,
        errorMessage: null
      };
    case FAILURE(ACTION_TYPES.GET_DOCUMENT_DOCX):
    case FAILURE(ACTION_TYPES.GET_DOCUMENT_PDF):
      return {
        ...state,
        errorMessage: action.payload,
        generatingDocument: false,
        printDocumentBlob: null,
        downloadDocumentBlob: null
      };
    case SUCCESS(ACTION_TYPES.GET_DOCUMENT_DOCX):
      return {
        ...state,
        generatingDocument: false,
        downloadDocumentBlob: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.GET_DOCUMENT_PDF):
      return {
        ...state,
        generatingDocument: false,
        printDocumentBlob: action.payload.data
      };

    default:
      return state;
  }
};

export const getDocument = (path, docName, entityId, type) => {
  if (type === 'DOCX') {
    return {
      type: ACTION_TYPES.GET_DOCUMENT_DOCX,
      payload: axios.get(`api/${path}/${entityId}/document?documentName=${docName}&type=${type}`, { responseType: 'blob' })
    };
  } else {
    return {
      type: ACTION_TYPES.GET_DOCUMENT_PDF,
      payload: axios.get(`api/${path}/${entityId}/document?documentName=${docName}&type=${type}`, { responseType: 'blob' })
    };
  }
};
