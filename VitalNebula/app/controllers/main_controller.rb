class MainController < ApplicationController
  def index
    @heartrates = Vital.where(device: "moto360", sensor: "heartrate").where("value <> 0").order("created_at asc")
    @temperatures = Bath.where(sensor: "temperature").where("value <> 0").order("created_at asc").where("id > 100")
  end

  def vital
    prev_id = params[:prev_id]
    @heartrates = Vital.where(device: "moto360", sensor: "heartrate").where("id > '#{prev_id}'").where("value <> 0").order("created_at asc")
    render :json => @heartrates, :methods => :formatted_date
  end

  def bath
    prev_id = params[:prev_id]
    @temperatures = Bath.where(sensor: "temperature").where("id > '#{prev_id}'").where("value <> 0").order("created_at asc")
    render :json => @temperatures, :methods => :formatted_date
  end
end
