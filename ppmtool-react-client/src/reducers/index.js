import { combineReducers } from "redux";
import errorReducer from "./errorReducer";
import projectReducer from "./projectReducer.js";

export default combineReducers({
  errors: errorReducer,
  project: projectReducer
});
